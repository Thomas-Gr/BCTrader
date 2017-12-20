package kraken.runnable;

import static kraken.core.SimpleKrakenProvider.getKraken;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import kraken.core.SimpleTrader;
import kraken.interfaces.Kraken;
import kraken.interfaces.Trader;
import kraken.types.ResultHistoryList;

public class FeedbackRunnable extends Runnable {

    private final Kraken kraken;
    private final Trader trader;

    public FeedbackRunnable() throws Exception {
        super(20, 20);
        this.trader = new SimpleTrader();
        this.kraken = getKraken();
    }

    @Override
    protected void runOnce() throws Exception {
        Map<String, Set<Double>> sold = new HashMap<>();
        Map<String, Set<Double>> bought = new HashMap<>();
        Map<String, Set<Double>> fees = new HashMap<>();

        Set<ResultHistoryList> history = kraken.getTodaysHistory();

        for (ResultHistoryList value : history) {
            if (value.getType().equals("sell")) {
                updateSum(sold, value.getPair(), Double.valueOf(value.getCost()));
                updateSum(fees, value.getPair(), Double.valueOf(value.getCost()) * 0.0016);
            } else {
                updateSum(bought, value.getPair(), Double.valueOf(value.getCost()));
                updateSum(fees, value.getPair(), Double.valueOf(value.getFee()));
            }
        }

        for (String key : sold.keySet()) {
            double boughtSum = bought.containsKey(key)
                    ? bought.get(key).stream().mapToDouble(Double::doubleValue).sum()
                    : 0;
            double soldSum = sold.get(key).stream().mapToDouble(Double::doubleValue).sum();
            double feesSum = fees.get(key).stream().mapToDouble(Double::doubleValue).sum();
            System.out.println(String.format(
                    "%s: Sold: %s (%s), Bought: %s (%s), Fees: %s => %s",
                    key,
                    soldSum,
                    sold.get(key).size(),
                    boughtSum,
                    bought.containsKey(key)
                            ? bought.get(key).size()
                            : 0,
                    feesSum,
                    soldSum - boughtSum - feesSum));
        }
    }

    private void updateSum(Map<String, Set<Double>> map, String pair, double cost) {
        if (!map.containsKey(pair)) {
            map.put(pair, new HashSet<>());
        }
        map.get(pair).add(cost);
    }

}
