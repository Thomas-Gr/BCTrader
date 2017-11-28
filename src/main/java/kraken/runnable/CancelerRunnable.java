package kraken.runnable;

import static kraken.core.SimpleKrakenProvider.getKraken;

import kraken.interfaces.Kraken;

public class CancelerRunnable extends Runnable {

    private final Kraken kraken;

    public CancelerRunnable() throws Exception {
        super(60, 60);
        this.kraken = getKraken();
    }

    @Override
    protected void runOnce() throws Exception {
        kraken.refreshOpenOrders();

        kraken.cancelOldQueries();
    }

}
