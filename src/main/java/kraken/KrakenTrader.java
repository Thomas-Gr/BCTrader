package kraken;

import kraken.runnable.TradingRunnable;

public class KrakenTrader {

    public static void main(String[] args) throws Exception {
        if (args.length == 7) {
            Constants.SOURCE = args[0];
            Constants.VOLUME = Double.valueOf(args[1]);
            Constants.LIMIT = Double.valueOf(args[2]);
            Constants.INITIAL_MONEY = Double.valueOf(args[3]);
            Constants.MACD_MIN = Double.valueOf(args[4]);
            Constants.PERCENTAGE_BEFORE_BUYING = Double.valueOf(args[5]);
            Constants.PERCENTAGE_BEFORE_SELLING = Double.valueOf(args[6]);
        } else {
            throw new IllegalArgumentException("Arguments should be: " +
                    "SOURCE VOLUME LIMIT INITIAL_MONEY MACD_MIN " +
                    "PERCENTAGE_BEFORE_BUYING PERCENTAGE_BEFORE_SELLING");
        }

        (new Thread(new TradingRunnable())).start();
    }

}
