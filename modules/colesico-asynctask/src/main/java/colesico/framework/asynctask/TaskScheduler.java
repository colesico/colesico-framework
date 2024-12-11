package colesico.framework.asynctask;

import java.util.Collection;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Scheduled task execution
 */
public interface TaskScheduler extends TaskExecutor {

    <T, R> Collection<Future<R>> schedule(T task, long delay, TimeUnit unit);

    <T> void scheduleAtFixedRate(T task, long initialDelay, long period, TimeUnit unit);

    <T> void scheduleWithFixedDelay(T task, long initialDelay, long delay, TimeUnit unit);
}
