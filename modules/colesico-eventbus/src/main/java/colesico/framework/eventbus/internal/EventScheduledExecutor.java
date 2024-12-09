package colesico.framework.eventbus.internal;

import colesico.framework.eventbus.AbstractEventExecutorConfig;
import colesico.framework.eventbus.EventDispatcher;
import colesico.framework.eventbus.EventSchedulerConfigPrototype;

import java.util.concurrent.*;

/**
 * Tasks schedule
 */
public final class EventScheduledExecutor extends EventExecutor {

    private final EventSchedulerConfigPrototype config;

    private final ThreadFactory threadFactory;

    private final ScheduledExecutorService executorService;

    public EventScheduledExecutor(EventDispatcher eventDispatcher, EventSchedulerConfigPrototype config) {
        super(eventDispatcher);
        this.config = config;

        this.threadFactory = new TaskThreadFactory("ETS-" + config.getPayloadType().getSimpleName());
        this.executorService = new ScheduledThreadPoolExecutor(config.getCorePoolSize(), threadFactory);
    }

    @Override
    protected ExecutorService getExecutorService() {
        return executorService;
    }

    @Override
    protected AbstractEventExecutorConfig getConfig() {
        return config;
    }

    public <P> void schedule(P taskPayload, long delay, TimeUnit unit) {
        executorService.schedule(createTask(taskPayload), delay, unit);
    }

    public <P> void scheduleAtFixedRate(P taskPayload, long initialDelay, long period, TimeUnit unit) {
        executorService.scheduleAtFixedRate(createTask(taskPayload), initialDelay, period, unit);
    }

    public <P> void scheduleWithFixedDelay(P taskPayload, long initialDelay, long delay, TimeUnit unit) {
        executorService.scheduleWithFixedDelay(createTask(taskPayload), initialDelay, delay, unit);
    }
}
