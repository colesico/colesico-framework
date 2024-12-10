package colesico.framework.task.internal;

import colesico.framework.task.AbstractTaskExecutorConfig;
import colesico.framework.task.TaskScheduler;
import colesico.framework.task.TaskSchedulerConfigPrototype;
import colesico.framework.task.registry.TaskRegistry;
import colesico.framework.task.registry.WorkersGroup;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.List;
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
        var workers = (WorkersGroup<T, R>) registry.getTaskWorkers(task.getClass());
        if (workers != null) {
            return workers.apply(worker -> executorService.schedule(createCallableTask(worker, task), delay, unit));
        }
        return List.of();
    }

    public <T> void scheduleAtFixedRate(T task, long initialDelay, long period, TimeUnit unit) {
        checkRunning();
        var workers = (WorkersGroup<T, ?>) registry.getTaskWorkers(task.getClass());
        if (workers != null) {
            workers.apply(worker -> executorService.scheduleAtFixedRate(createRunnableTask(worker, task), initialDelay, period, unit));
        }
    }

    public <T> void scheduleWithFixedDelay(T task, long initialDelay, long delay, TimeUnit unit) {
        checkRunning();
        var workers = (WorkersGroup<T, ?>) registry.getTaskWorkers(task.getClass());
        if (workers != null) {
            workers.apply(worker -> executorService.scheduleWithFixedDelay(createRunnableTask(worker, task), initialDelay, delay, unit));
        }
    }
}
