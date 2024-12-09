package colesico.framework.eventbus.internal;

import colesico.framework.eventbus.AbstractEventExecutorConfig;
import colesico.framework.eventbus.AsyncEventBus;
import colesico.framework.eventbus.EventSubmitterConfigPrototype;
import colesico.framework.eventbus.registry.EventRegistry;
import colesico.framework.eventbus.registry.ListenersGroup;

import java.util.concurrent.*;

/**
 * Event queue
 */
public final class AsyncEventBusImpl extends EventExecutor implements AsyncEventBus {

    private final EventSubmitterConfigPrototype config;

    private final ThreadPoolExecutor executorService;

    public AsyncEventBusImpl(EventRegistry eventRegistry, EventSubmitterConfigPrototype config) {
        super(eventRegistry);
        this.config = config;

        ThreadFactory threadFactory = new TaskThreadFactory("COLESICO-EVENT-ASYNC");

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

    public <E> void submit(final E event) {
        var listeners = (ListenersGroup<E>) registry.getEventListeners(event.getClass());
        if (listeners != null) {
            listeners.apply(listener -> getExecutorService().submit(createTask(listener, event)));
        }
    }
}
