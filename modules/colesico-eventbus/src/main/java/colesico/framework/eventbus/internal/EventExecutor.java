package colesico.framework.eventbus.internal;

import colesico.framework.eventbus.AbstractEventExecutorConfig;
import colesico.framework.eventbus.registry.EventListener;
import colesico.framework.eventbus.registry.EventRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Abstract basic event executor  based on {@link ExecutorService}
 */
abstract public class EventExecutor {

    protected static final Logger logger = LoggerFactory.getLogger(EventExecutor.class);

    protected final EventRegistry registry;

    abstract protected ExecutorService getExecutorService();

    abstract protected AbstractEventExecutorConfig getConfig();

    public EventExecutor(EventRegistry registry) {
        this.registry = registry;
    }

    protected final <E> Runnable createTask(final EventListener<E> listener, final E event) {
        final long enqueueTime = System.currentTimeMillis();
        logger.debug("New task queued: {}", event);
        return () -> {
            if (logger.isDebugEnabled()) {
                final long queueingDuration = System.currentTimeMillis() - enqueueTime;
                logger.debug("Task ready to be executed. Duration being in queue {}", queueingDuration);
            }
            listener.consume(event);
        };
    }

    public final void shutdown() {
        getExecutorService().shutdown();
    }

    public final void awaitTermination(long seconds) {
        try {
            boolean taskCompleted = getExecutorService().awaitTermination(seconds, TimeUnit.SECONDS);
            if (!taskCompleted) {
                logger.info("Some tasks were not completed for payload: " + getConfig().getPayloadType().getSimpleName());
                final List<Runnable> rejected = getExecutorService().shutdownNow();
                logger.info("Rejected tasks: " + rejected.size());
            }
        } catch (InterruptedException e) {
            logger.error("Await termination interrupted");
        }
    }
}
