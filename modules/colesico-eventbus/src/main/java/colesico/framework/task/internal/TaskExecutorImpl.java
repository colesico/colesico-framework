package colesico.framework.task.internal;

import colesico.framework.task.TaskExecutor;
import colesico.framework.task.TaskExecutorConfigPrototype;
import colesico.framework.task.TaskSubmitterConfigPrototype;
import colesico.framework.task.registry.TaskRegistry;

import java.util.concurrent.*;

/**
 * Task queue
 */
public final class TaskExecutorImpl extends AbstractTaskExecutor implements TaskExecutor {

    private final TaskSubmitterConfigPrototype config;

    private final ThreadPoolExecutor executorService;

    public TaskExecutorImpl(TaskRegistry taskRegistry, TaskSubmitterConfigPrototype config) {
        super(taskRegistry);
        this.config = config;

        ThreadFactory threadFactory = new TaskThreadFactory("COLESICO-TASK-ASYNC");

        BlockingQueue<Runnable> queue;
        if (config.getQueueCapacity() <= 0) {
            queue = new LinkedBlockingQueue<>();
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
    protected TaskExecutorConfigPrototype getConfig() {
        return config;
    }



}
