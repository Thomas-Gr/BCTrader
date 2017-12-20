package kraken.interfaces;

import java.util.List;
import java.util.Set;

import kraken.types.OHLC;
import kraken.types.ResultHistoryList;

public interface Kraken {

    double volumeOfInflightOrders();
    void buyThenSell(int userId, double volume, double buyPrice, double sellPrice);

    boolean canBuy(double volume, double price, Double sellPrice);

    List<OHLC> getPreviousResults();
    Set<ResultHistoryList> getTodaysHistory();
    void refreshOpenOrders();
    void cancelOldQueries();
}
