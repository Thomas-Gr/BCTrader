package kraken.runnable;

import static kraken.Constants.VOLUME;
import static kraken.core.SimpleKrakenProvider.getKraken;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import kraken.interfaces.Kraken;
import kraken.core.SimpleTrader;
import kraken.interfaces.Trader;
import kraken.types.OHLC;

public class TradingRunnable extends Runnable {

    private static final Logger logger = LogManager.getLogger(TradingRunnable.class);

    private final Kraken kraken;
    private final Trader trader;

    public TradingRunnable() throws Exception {
        super(20, 120);
        this.trader = new SimpleTrader();
        this.kraken = getKraken();
    }

    @Override
    protected void runOnce() throws Exception {
        List<OHLC> latestResults = kraken.getPreviousResults();

        double currentValue = latestResults.get(0).getClose();

        Optional<Double> shouldBuy = trader.shouldBuy(currentValue, latestResults);
        if (shouldBuy.isPresent()) {
            trader.informBuy();
            if (kraken.canBuy(VOLUME, currentValue, shouldBuy.get())) {
                kraken.buyThenSell(42, VOLUME, currentValue, shouldBuy.get());
                logger.info(String.format("Buy at %s", currentValue));
            } else {
                logger.info(String.format("Should but can't buy at %s", currentValue));
            }
        } else {
            logger.info(String.format("Nothing to do at %s", currentValue));
        }
    }

}
