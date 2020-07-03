package colesico.framework.asynctask.internal;

import colesico.framework.asynctask.TaskExecutorConfigBase;
import colesico.framework.asynctask.TaskQueueConfigPrototype;

import java.util.concurrent.*;

/**
 * Tasks queue
 */
public final class TaskQueueExecutor extends TaskExecutor {

    private final TaskQueueConfigPrototype config;

    private final ThreadFactory threadFactory;
    private final BlockingQueue<Runnable> queue;
    private final ThreadPoolExecutor executorService;

    public TaskQueueExecutor(DefaultConsumer defaultConsumer, TaskQueueConfigPrototype config) {
        super(defaultConsumer);
        this.config = config;

        this.threadFactory = new TaskThreadFactory("TQ-" + config.getPayloadType().getSimpleName());

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
    protected TaskExecutorConfigBase getConfig() {
        return config;
    }
}
