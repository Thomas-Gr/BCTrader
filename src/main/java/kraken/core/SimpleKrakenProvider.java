package kraken.core;

import static kraken.Constants.SOURCE;

import kraken.interfaces.Kraken;

public class SimpleKrakenProvider {

    private static final Kraken kraken = new SimpleKraken(SOURCE, "EUR");

    public static Kraken getKraken() {
        return kraken;
    }

}
