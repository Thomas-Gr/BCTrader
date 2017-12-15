package kraken.core;

import static kraken.util.RetryerHelper.exponentialRetry;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import kraken.interfaces.Kraken;
import kraken.types.OHLC;

public class RetryerKraken implements Kraken {

    private static final Logger logger = LogManager.getLogger(RetryerKraken.class);

    private final Kraken kraken;

    public RetryerKraken(Kraken kraken) {
        this.kraken = kraken;
    }

    @Override
    public double volumeOfInflightOrders() {
        return exponentialRetry(kraken::volumeOfInflightOrders);
    }

    @Override
    public void buyThenSell(int userId, double volume, double buyPrice, double sellPrice) {
        try {
            kraken.buyThenSell(userId, volume, buyPrice, sellPrice);
        } catch (Exception e) {
            logger.info("Buying failed, but no retry is configured");
        }
    }

    @Override
    public boolean canBuy(double volume, double cost, Double sellPrice) {
        return exponentialRetry(() -> kraken.canBuy(volume, cost, sellPrice));
    }

    @Override
    public List<OHLC> getPreviousResults() {
        return exponentialRetry(kraken::getPreviousResults);
    }

    @Override
    public void closedOrders() {
        exponentialRetry(kraken::closedOrders);
    }

    @Override
    public void refreshOpenOrders() {
        exponentialRetry(kraken::refreshOpenOrders);
    }

    @Override
    public void cancelOldQueries() {
        exponentialRetry(kraken::cancelOldQueries);
    }

}
