package colesico.framework.eventbus;

import java.util.concurrent.TimeUnit;

/**
 * Scheduled event execution
 */
public interface EventScheduler {

    <E> void schedule(E event, long delay, TimeUnit unit);

    <E> void scheduleAtFixedRate(E event, long initialDelay, long period, TimeUnit unit);

    <E> void scheduleWithFixedDelay(E event, long initialDelay, long delay, TimeUnit unit);
}
