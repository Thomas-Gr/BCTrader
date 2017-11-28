package kraken.util;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

public final class Calculator {

    private Calculator() {}

    public static List<Double> computeLatestMACD(List<Double> closeResults) {
        List<Double> ema26 = ema(closeResults, 26);
        List<Double> ema12 = ema(closeResults, 12);

        List<Double> macd = computeDiff(ema26, ema12);

        List<Double> signal = ema(macd, 9);
        List<Double> histogram = computeDiff(signal, macd);

        return histogram;
    }

    public static double computeMACD(List<Double> closeResults) {
        List<Double> ema26 = ema(closeResults, 26);
        List<Double> ema12 = ema(closeResults, 12);

        List<Double> macd = computeDiff(ema26, ema12);

        List<Double> signal = ema(macd, 9);
        List<Double> histogram = computeDiff(signal, macd);

        return histogram.get(0);
    }

    private static List<Double> computeDiff(List<Double> left, List<Double> right) {
        List<Double> diff = new ArrayList<>();

        for (int i = 0; i < left.size() && i < right.size(); i++) {
            diff.add(right.get(i) - left.get(i));
        }

        return diff;
    }

    private static List<Double> ema(List<Double> results, int order) {
        List<Double> result = new ArrayList<>();

        double ema = sma(results.subList(results.size() - order, results.size()));
        result.add(ema);
        for (int i = results.size() - order - 1; i >= 0; i--) {
            Double currentValue = results.get(i);
            ema = ratio(currentValue, ema, order);
            result.add(ema);
        }

        return Lists.reverse(result);
    }

    private static double ratio(double newValue, double oldValue, int size) {
        return newValue * 2. / (size + 1) + oldValue * (1. - 2. / (size + 1));
    }

    private static double sma(List<Double> results) {
        return results.stream().mapToDouble(a -> a).average().getAsDouble();
    }
}