package kraken.core;

import static kraken.Constants.BLOCKCHAIN_TO_CONSIDER;
import static kraken.Constants.PERCENTAGE_DIFF_BEFORE_BUY_AGAIN;
import static kraken.Constants.LIMIT;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import kraken.api.KrakenApi;
import kraken.api.KrakenApi.Method;
import kraken.interfaces.Kraken;
import kraken.types.History;
import kraken.types.OHLC;
import kraken.types.OpenOrder;
import kraken.types.ResultClosedTradesList;
import kraken.types.ResultHistoryList;
import kraken.types.Trades;
import kraken.util.JsonParser;

public class SimpleKraken implements Kraken {
    private static final Logger logger = LogManager.getLogger(SimpleKraken.class);

    private static final Pattern EURO_BALANCE = Pattern.compile(".*ZEUR\":\"([0-9]+\\.[0-9]+)\".*");
    private static final int AGE_BEFORE_CANCELLING = 600;
    private static final double TAX = 0.0016;

    private final KrakenApi api = new KrakenApi(
            "xxxx",
            "xxxx");

    private final String source;
    private final String target;
    private final String pair;

    private OpenOrder openOrders;

    public SimpleKraken(String source, String target) {
        this.source = source;
        this.target = target;
        this.pair = String.format("X%sZ%s", source, target);
    }

    @Override
    public double volumeOfInflightOrders() {
        refreshOpenOrders();

        return openOrders.getResult().getClosed().values().stream()
                .filter(order -> order.getDescr().get("type").equals("buy"))
                .map(order -> Double.valueOf(order.getVol()) * Double.valueOf(order.getDescr().get("price")))
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    @Override
    public void buyThenSell(int userId, double volume, double buyPrice, double sellPrice) {
        try {
            logger.info(String.format(
                    "Buying %s %s at %s to sell at %s (+%.3f euros)",
                    volume,
                    source,
                    buyPrice,
                    sellPrice,
                    gain(volume, buyPrice, sellPrice)));

            Map<String, String> input = ImmutableMap.<String, String>builder()
                    .put("pair", pair)
                    .put("type", "buy")
                    .put("oflags", "post,fciq")
                    .put("ordertype", "limit")
                    .put("price", String.valueOf(buyPrice))
                    .put("volume", String.valueOf(volume))
                    .put("userref", String.valueOf(userId))
                    .put("close[ordertype]", "limit")
                    .put("close[price]", String.valueOf(sellPrice))
                    .build();

            String response = api.queryPrivate(Method.ADD_ORDER, input);

            logger.info(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static double gain(double volume, double buy, double sell) {
        return volume * (sell * (1 - TAX) - buy * (1 + TAX));
    }

    @Override
    public boolean canBuy(double volume, double cost, Double sellPrice) {
        try {
            String s = api.queryPrivate(Method.BALANCE);

            Matcher matcher = EURO_BALANCE.matcher(s);

            if (matcher.find()) {
                double inflight = volumeOfInflightOrders();
                Double euroBalance = Double.valueOf(matcher.group(1));

                Double balance = euroBalance - inflight;
                logger.info(String.format("Balance %s = %s - %s", balance, euroBalance, inflight));

                boolean hasAlreadyCloseOrder = openOrders.getResult().getClosed().entrySet().stream()
                        .filter(order -> order.getValue().getDescr().get("type").equals("sell"))
                        .map(order -> order.getValue().getDescr().get("price"))
                        .map(Double::valueOf)
                        .anyMatch(value -> Math.abs(value - sellPrice) < sellPrice * PERCENTAGE_DIFF_BEFORE_BUY_AGAIN);

                if (hasAlreadyCloseOrder) {
                    logger.info("There is already a close sell order for " + sellPrice);
                    return false;
                }

                if (balance > LIMIT) {
                    return true;
                }
            }

            logger.info("Couldn't find euro balance in: " + s);
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<OHLC> getPreviousResults() {
        try {
            Map<String, String> input = ImmutableMap.of("pair", source + target);

            String response = api.queryPublic(Method.OHLC, input);

            Trades trades = JsonParser.getObjectMapper().readValue(
                    response.replaceAll(pair, "numbers"),
                    Trades.class);

            return Lists.reverse(trades.getResult().getNumbers());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<ResultHistoryList> getTodaysHistory() {
        try {
            String yesterday = String.valueOf(Instant.now().toEpochMilli() / 1000 - 24 * 60 * 60);

            Set<ResultHistoryList> result = new HashSet<>();
            boolean hasNext;
            String response = api.queryPrivate(Method.TRADES_HISTORY);
            do {
                String lastTime = "ZZZZZ";
                hasNext = true;
                History history = JsonParser.getObjectMapper().readValue(
                        response,
                        History.class);

                for (ResultHistoryList transaction : history.getResult()
                        .getClosed()
                        .values()) {
                    if (transaction.getTime().compareTo(yesterday) > 0) {
                        result.add(transaction);
                    } else {
                        hasNext = false;
                    }

                    if (transaction.getTime().compareTo(lastTime) < 0) {
                        lastTime = transaction.getTime();
                    }
                }

                if (hasNext) {
                    response = api.queryPrivate(Method.TRADES_HISTORY, ImmutableMap.of("end", lastTime));
                }
            } while (hasNext);

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void refreshOpenOrders() {
            try {
            String response = api.queryPrivate(Method.OPEN_ORDERS);

            openOrders = JsonParser.getObjectMapper().readValue(
                    response,
                    OpenOrder.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancelOldQueries() {
        openOrders.getResult().getClosed().entrySet().stream()
                .filter(order -> order.getValue().getDescr().get("type").equals("buy"))
                .filter(order -> BLOCKCHAIN_TO_CONSIDER.contains(order.getValue().getDescr().get("pair")))
                .filter(SimpleKraken::isOldEnough)
                .map(Map.Entry::getKey)
                .forEach(key -> {
                    Map<String, String> input = ImmutableMap.of("txid", key);

                    try {
                        String result = api.queryPrivate(Method.CANCEL_ORDER, input);

                        logger.info("Cancelling order: " + result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private static boolean isOldEnough(Map.Entry<String, ResultClosedTradesList> order) {
        return System.currentTimeMillis() / 1000 - Double.valueOf(order.getValue().getOpentm()) > AGE_BEFORE_CANCELLING;
    }

}
