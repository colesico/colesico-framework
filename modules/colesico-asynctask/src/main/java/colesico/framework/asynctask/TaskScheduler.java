package colesico.framework.asynctask;

import java.util.concurrent.TimeUnit;

/**
 * Is used to create task execution schedule
 */
public interface TaskScheduler {
    <P> void schedule(P taskPayload, long delay, TimeUnit unit);

    <P> void scheduleAtFixedRate(P taskPayload, long initialDelay, long period, TimeUnit unit);

    <P> void scheduleWithFixedDelay(P taskPayload, long initialDelay, long delay, TimeUnit unit);
}
