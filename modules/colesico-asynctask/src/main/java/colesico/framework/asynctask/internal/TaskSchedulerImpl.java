package colesico.framework.asynctask.internal;

import colesico.framework.asynctask.AbstractTaskExecutorConfig;
import colesico.framework.asynctask.TaskScheduler;
import colesico.framework.asynctask.TaskSchedulerConfigPrototype;
import colesico.framework.asynctask.registry.TaskRegistry;
import colesico.framework.asynctask.registry.TaskWorker;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.concurrent.*;

/**
 * Tasks schedule
 */
@Singleton
public final class TaskSchedulerImpl extends AbstractTaskExecutor implements TaskScheduler {

    private final TaskSchedulerConfigPrototype config;

    private final ScheduledExecutorService executorService;

    @Inject
    public TaskSchedulerImpl(TaskRegistry taskRegistry, TaskSchedulerConfigPrototype config) {
        super(taskRegistry);
        this.config = config;

        ThreadFactory threadFactory = new TaskThreadFactory("COLESICO-TASKS-SHEDULED");
        this.executorService = new ScheduledThreadPoolExecutor(config.getCorePoolSize(), threadFactory);
    }

    @Override
    protected ExecutorService getExecutorService() {
        return executorService;
    }

    @Override
    protected AbstractTaskExecutorConfig getConfig() {
        return config;
    }

    public <T, R> Collection<Future<R>> schedule(T task, long delay, TimeUnit unit) {
        checkRunning();
        return registry.applyReturn(task.getClass(),
                worker -> executorService.schedule(createCallableTask((TaskWorker<T, R>) worker, task), delay, unit)
        );
    }

    public <T> void scheduleAtFixedRate(T task, long initialDelay, long period, TimeUnit unit) {
        checkRunning();
        registry.applyReturn(task.getClass(),
                worker -> executorService.scheduleAtFixedRate(createRunnableTask((TaskWorker<T, ?>) worker, task), initialDelay, period, unit)
        );
    }

    public <T> void scheduleWithFixedDelay(T task, long initialDelay, long delay, TimeUnit unit) {
        checkRunning();
        registry.applyReturn(task.getClass(),
                worker -> executorService.scheduleWithFixedDelay(createRunnableTask((TaskWorker<T, ?>) worker, task), initialDelay, delay, unit)
        );
    }
}
