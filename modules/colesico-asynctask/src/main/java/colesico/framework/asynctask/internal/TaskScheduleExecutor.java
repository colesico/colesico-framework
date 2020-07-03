package colesico.framework.asynctask.internal;

import colesico.framework.asynctask.TaskExecutorConfigBase;
import colesico.framework.asynctask.TaskScheduleConfigPrototype;

import java.util.concurrent.*;

/**
 * Tasks schedule
 */
public final class TaskScheduleExecutor extends TaskExecutor {

    private final TaskScheduleConfigPrototype config;

    private final ThreadFactory threadFactory;
    private final ScheduledExecutorService executorService;


    public TaskScheduleExecutor(DefaultConsumer defaultConsumer, TaskScheduleConfigPrototype config) {
        super(defaultConsumer);
        this.config = config;

        this.threadFactory = new TaskThreadFactory("TS-" + config.getPayloadType().getSimpleName());
        this.executorService = new ScheduledThreadPoolExecutor(config.getCorePoolSize(), threadFactory);
    }

    @Override
    protected ExecutorService getExecutorService() {
        return executorService;
    }

    @Override
    protected TaskExecutorConfigBase getConfig() {
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
