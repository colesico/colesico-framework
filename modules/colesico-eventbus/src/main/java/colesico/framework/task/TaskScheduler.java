package colesico.framework.task;

import java.util.concurrent.TimeUnit;

/**
 * Scheduled task execution
 */
public interface TaskScheduler extends TaskExecutor {

    <E> void schedule(E task, long delay, TimeUnit unit);

    <E> void scheduleAtFixedRate(E task, long initialDelay, long period, TimeUnit unit);

    <E> void scheduleWithFixedDelay(E task, long initialDelay, long delay, TimeUnit unit);
}
