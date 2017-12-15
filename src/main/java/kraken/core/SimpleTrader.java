package kraken.core;

import static java.util.Optional.empty;
import static java.util.stream.Collectors.toList;
import static kraken.Constants.MACD_MIN;
import static kraken.Constants.PERCENTAGE_BEFORE_BUYING;
import static kraken.Constants.PERCENTAGE_BEFORE_SELLING;
import static kraken.util.Calculator.computeMACD;

import java.util.List;
import java.util.Optional;

import kraken.interfaces.Trader;
import kraken.types.OHLC;

public class SimpleTrader implements Trader {

    private static final int INVESTIGATION_PERIOD = 90;
    private static final int MINUTES_BEFORE_NEW_BUY = 4;
    private int timeBeforeBuying = 0;

    @Override
    public Optional<Double> shouldBuy(double currentValue, List<OHLC> previousValues) {
        if (timeBeforeBuying-- <= 0 && previousValues.size() >= INVESTIGATION_PERIOD) {
            double macd = computeMACD(previousValues.stream().map(OHLC::getClose).collect(toList()));
            double minimumPrice = currentValue * (1 + PERCENTAGE_BEFORE_BUYING);

            if (macd < MACD_MIN && isLowerThanBefore(previousValues, minimumPrice)) {
                return Optional.of(getSellPrice(currentValue));
            }
        }

        return empty();
    }

    private boolean isLowerThanBefore(List<OHLC> previousValues, double minimumSmallPrice) {
        return previousValues.subList(0, INVESTIGATION_PERIOD).stream()
                .map(OHLC::getClose)
                .anyMatch(price -> price > minimumSmallPrice);
    }

    @Override
    public void informBuy() {
        timeBeforeBuying = MINUTES_BEFORE_NEW_BUY;
    }

    private double getSellPrice(double currentValue) {
        return currentValue * (1 + PERCENTAGE_BEFORE_SELLING);
    }
}