package colesico.framework.task;

import java.util.concurrent.TimeUnit;

/**
 * Scheduled task execution
 */
public interface TaskScheduler extends TaskExecutor {

    <T> void schedule(T task, long delay, TimeUnit unit);

    <T> void scheduleAtFixedRate(T task, long initialDelay, long period, TimeUnit unit);

    <T> void scheduleWithFixedDelay(T task, long initialDelay, long delay, TimeUnit unit);
}
