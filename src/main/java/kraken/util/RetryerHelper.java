package kraken.util;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.github.rholder.retry.Attempt;
import com.github.rholder.retry.RetryListener;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RetryerHelper {

    private static final Logger logger = LogManager.getLogger(RetryerHelper.class);

    public static void exponentialRetry(Runnable runnable){
        exponentialRetry(() -> { runnable.run(); return 0; });
    }

    public static <T> T exponentialRetry(Callable<T> callable) {
        try {
            Retryer<T> build = RetryerBuilder.<T>newBuilder()
                    .retryIfRuntimeException()
                    .withWaitStrategy(WaitStrategies.exponentialWait(1000, 5, TimeUnit.MINUTES))
                    .withStopStrategy(StopStrategies.stopAfterAttempt(5))
                    .withRetryListener(new RetryListener() {
                        @Override
                        public <V> void onRetry(Attempt<V> attempt) {
                            if (attempt.getAttemptNumber() > 1) {
                                logger.info("Failed attempt " + attempt.getAttemptNumber());
                            }
                        }
                    })
                    .build();

            return build.call(callable);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
