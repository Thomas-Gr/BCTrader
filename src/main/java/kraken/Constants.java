package kraken;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

public class Constants {
    public static String SOURCE = "XRP";
    public static double VOLUME = 600;
    public static double LIMIT = 1100;
    public static double INITIAL_MONEY = 1500;
    public static double MACD_MIN = 0.00001;// -0.0001; // -0.05
    public static double PERCENTAGE_BEFORE_BUYING = 0.01;
    public static double PERCENTAGE_BEFORE_SELLING = 0.01;
    public static Set<String> BLOCKCHAIN_TO_CONSIDER = ImmutableSet.of("ETHEUR", "LTCEUR");
}
