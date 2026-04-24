package colesico.framework.asynctask.internal;

import colesico.framework.asynctask.AbstractTaskExecutorConfig;
import colesico.framework.asynctask.TaskExecutor;
import colesico.framework.asynctask.TaskExecutorConfigPrototype;
import colesico.framework.asynctask.registry.TaskRegistry;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
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
        if (config.queueCapacity() <= 0) {
            queue = new LinkedBlockingQueue<>();
        } else {
            queue = new ArrayBlockingQueue<>(config.queueCapacity());
        }

        executorService = new ThreadPoolExecutor(
                config.corePoolSize(),
                config.maximumPoolSize(),
                config.keepAliveTime(),
                TimeUnit.MILLISECONDS,
                queue,
                threadFactory);
    }

    @Override
    protected ExecutorService executorService() {
        return executorService;
    }

    @Override
    protected AbstractTaskExecutorConfig config() {
        return config;
    }


}
