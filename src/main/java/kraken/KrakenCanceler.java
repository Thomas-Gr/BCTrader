package kraken;

import kraken.runnable.CancelerRunnable;

public class KrakenCanceler {

    public static void main(String[] args) throws Exception {
        (new Thread(new CancelerRunnable())).start();
    }

}
