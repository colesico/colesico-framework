package colesico.framework.taskhub.internal;

import colesico.framework.taskhub.AbstractEventExecutorConfig;
import colesico.framework.taskhub.EventDispatcher;
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

    private final EventDispatcher dispatcher;

    abstract protected ExecutorService getExecutorService();

    abstract protected AbstractEventExecutorConfig getConfig();

    public EventExecutor(EventDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    protected final <E> Runnable createTask(final E event) {
        final long enqueueTime = System.currentTimeMillis();
        logger.debug("New task queued: {}", event);
        return () -> {
            if (logger.isDebugEnabled()) {
                final long queueingDuration = System.currentTimeMillis() - enqueueTime;
                logger.debug("Task ready to be executed. Duration being in queue {}", queueingDuration);
            }
            dispatcher.dispatch(event);
        };
    }

    public final <E> void submit(final E event) {
        getExecutorService().submit(createTask(event));
    }

    public final void stop() {
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
