package kraken;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

public class Constants {
    public static String SOURCE = "ETH";
    public static double VOLUME = 1;
    public static double LIMIT = 1050;
    public static double INITIAL_MONEY = 270;
    public static double MACD_MIN = -0.05;// -0.0001; // -0.05
    public static double PERCENTAGE_BEFORE_BUYING = 0.008;
    public static double PERCENTAGE_BEFORE_SELLING = 0.008;
    public static Set<String> BLOCKCHAIN_TO_CONSIDER = ImmutableSet.of("ETHEUR", "LTCEUR");
}
