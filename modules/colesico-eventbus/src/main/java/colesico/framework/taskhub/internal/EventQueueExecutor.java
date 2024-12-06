package colesico.framework.taskhub.internal;

import colesico.framework.taskhub.AbstractEventExecutorConfig;
import colesico.framework.taskhub.EventDispatcher;
import colesico.framework.taskhub.EventSubmitterConfigPrototype;

import java.util.concurrent.*;

/**
 * Tasks queue
 */
public final class EventQueueExecutor extends EventExecutor {

    private final EventSubmitterConfigPrototype config;

    private final ThreadPoolExecutor executorService;

    public EventQueueExecutor(EventDispatcher eventDispatcher, EventSubmitterConfigPrototype config) {
        super(eventDispatcher);
        this.config = config;

        ThreadFactory threadFactory = new TaskThreadFactory("TQ-" + config.getPayloadType().getSimpleName());

        BlockingQueue<Runnable> queue;
        if (config.getQueueCapacity() <= 0) {
            queue = new LinkedBlockingQueue();
        } else {
            queue = new ArrayBlockingQueue<>(config.getQueueCapacity());
        }

        executorService = new ThreadPoolExecutor(
                config.getCorePoolSize(),
                config.getMaximumPoolSize(),
                config.getKeepAliveTime(),
                TimeUnit.MILLISECONDS, queue, threadFactory);
    }

    @Override
    protected ExecutorService getExecutorService() {
        return executorService;
    }

    @Override
    protected AbstractEventExecutorConfig getConfig() {
        return config;
    }
}
