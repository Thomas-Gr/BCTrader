package kraken.interfaces;

import java.util.List;
import java.util.Optional;

import kraken.types.OHLC;

public interface Trader {

    Optional<Double> shouldBuy(double currentValue, List<OHLC> previousValues);
    void informBuy();

}