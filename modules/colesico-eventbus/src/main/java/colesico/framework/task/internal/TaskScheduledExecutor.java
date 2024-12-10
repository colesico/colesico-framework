package colesico.framework.task.internal;

import colesico.framework.task.TaskExecutorConfigPrototype;
import colesico.framework.taskbus.TaskDispatcher;
import colesico.framework.task.TaskSchedulerConfigPrototype;

import java.util.concurrent.*;

/**
 * Tasks schedule
 */
public final class TaskScheduledExecutor extends AbstractTaskExecutor {

    private final TaskSchedulerConfigPrototype config;

    private final ThreadFactory threadFactory;

    private final ScheduledExecutorService executorService;

    public TaskScheduledExecutor(TaskDispatcher taskDispatcher, TaskSchedulerConfigPrototype config) {
        super(taskDispatcher);
        this.config = config;

        this.threadFactory = new TaskThreadFactory("ETS-" + config.getPayloadType().getSimpleName());
        this.executorService = new ScheduledThreadPoolExecutor(config.getCorePoolSize(), threadFactory);
    }

    @Override
    protected ExecutorService getExecutorService() {
        return executorService;
    }

    @Override
    protected TaskExecutorConfigPrototype getConfig() {
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
