package colesico.framework.asynctask.internal;

import colesico.framework.asynctask.TaskConsumer;
import colesico.framework.asynctask.TaskPublisher;
import colesico.framework.asynctask.TaskQueueConfigPrototype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;

/**
 * Tasks queue based on {@link ExecutorService}
 */
public final class TaskQueue implements TaskPublisher {

    private static final Logger logger = LoggerFactory.getLogger(TaskQueue.class);

    private final TaskQueueConfigPrototype config;
    private final BlockingQueue<Runnable> queue;
    private final ExecutorService executorService;
    private final ThreadFactory threadFactory;
    private final DefaultConsumer defaultConsumer;

    public TaskQueue(TaskQueueConfigPrototype config, DefaultConsumer defaultConsumer) {
        this.config = config;
        this.defaultConsumer = defaultConsumer;

        if (config.getQueueCapacity() <= 0) {
            queue = new LinkedBlockingQueue();
        } else {
            queue = new ArrayBlockingQueue<>(config.getQueueCapacity());
        }

        threadFactory = new TaskThreadFactory("TQ-" + config.getPayloadType().getSimpleName());

        executorService = new ThreadPoolExecutor(
                config.getCorePoolSize(),
                config.getMaximumPoolSize(),
                config.getKeepAliveTime(),
                TimeUnit.MILLISECONDS, queue, threadFactory);
    }

    public BlockingQueue<Runnable> getQueue() {
        return queue;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    @Override
    public <P> void enqueue(final P taskPayload) {
        final long enqueueTime = System.currentTimeMillis();
        executorService.submit(() -> {
            final long queueingDuration = System.currentTimeMillis() - enqueueTime;
            logger.debug("Task queuing duration {}", queueingDuration);
            TaskConsumer consumer = config.getTaskConsumer();
            if (consumer != null) {
                consumer.consume(taskPayload);
            } else {
                defaultConsumer.consume(taskPayload);
            }
        });
    }

    public void stop() {
        executorService.shutdown();
    }

    public void awaitTermination(long seconds) {
        try {
            boolean taskCompleted = executorService.awaitTermination(seconds, TimeUnit.SECONDS);
            if (!taskCompleted) {
                logger.info("Some tasks were not completed for queue: " + config.getPayloadType().getSimpleName());
                final List<Runnable> rejected = executorService.shutdownNow();
                // TODO: dump task payloads
            }
        } catch (InterruptedException e) {
            logger.error("Await termination interrupted");
        }
    }
}
