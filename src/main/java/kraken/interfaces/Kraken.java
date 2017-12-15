package kraken.interfaces;

import java.util.List;

import kraken.types.OHLC;

public interface Kraken {

    double volumeOfInflightOrders();
    void buyThenSell(int userId, double volume, double buyPrice, double sellPrice);

    boolean canBuy(double volume, double price, Double sellPrice);

    List<OHLC> getPreviousResults();
    void closedOrders();
    void refreshOpenOrders();
    void cancelOldQueries();
}
