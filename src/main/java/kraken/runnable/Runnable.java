package kraken.runnable;

public abstract class Runnable extends Thread {

    private final long secondsBetweenRun;
    private final long secondsBetweenExceptions;

    public Runnable(long secondsBetweenRun, long secondsBetweenExceptions) throws Exception {
        this.secondsBetweenRun = secondsBetweenRun;
        this.secondsBetweenExceptions = secondsBetweenExceptions;
    }

    @Override
    public void run() {
        try {
            while (true) {
                try {
                    runOnce();

                    Thread.sleep(secondsBetweenRun * 1_000L);
                } catch (Exception e) {
                    e.printStackTrace();
                    Thread.sleep(secondsBetweenExceptions * 1_000L);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void runOnce() throws Exception;

}
