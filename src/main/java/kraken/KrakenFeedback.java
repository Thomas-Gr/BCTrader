package kraken;

import kraken.runnable.FeedbackRunnable;

public class KrakenFeedback {

    public static void main(String[] args) throws Exception {
        (new Thread(new FeedbackRunnable())).start();
    }

}
