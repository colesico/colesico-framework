package colesico.framework.task.internal;

import colesico.framework.task.AbstractTaskExecutorConfig;
import colesico.framework.task.TaskExecutor;
import colesico.framework.task.TaskExecutorConfigPrototype;
import colesico.framework.task.registry.TaskRegistry;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.*;

/**
 * Task queue
 */
@Singleton
public final class TaskExecutorImpl extends AbstractTaskExecutor implements TaskExecutor {

    private final TaskExecutorConfigPrototype config;

    private final ThreadPoolExecutor executorService;

    @Inject
    public TaskExecutorImpl(TaskRegistry taskRegistry, TaskExecutorConfigPrototype config) {
        super(taskRegistry);
        this.config = config;

        ThreadFactory threadFactory = new TaskThreadFactory("COLESICO-TASKS-ASYNC");

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
                TimeUnit.MILLISECONDS,
                queue,
                threadFactory);
    }

    @Override
    protected ExecutorService getExecutorService() {
        return executorService;
    }

    @Override
    protected AbstractTaskExecutorConfig getConfig() {
        return config;
    }


}
