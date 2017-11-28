package kraken.interfaces;

import java.util.List;

import kraken.types.OHLC;

public interface Kraken {

    double volumeOfInflightOrders() throws Exception;
    void buyThenSell(int userId, double volume, double buyPrice, double sellPrice) throws Exception;

    boolean canBuy(double volume, double price, Double sellPrice) throws Exception;

    List<OHLC> getPreviousResults() throws Exception;
    void closedOrders() throws Exception;
    void refreshOpenOrders() throws Exception;
    void cancelOldQueries() throws Exception;
}
