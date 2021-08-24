package colesico.framework.asynctask.internal;

import colesico.framework.asynctask.TaskPerformer;
import colesico.framework.asynctask.TaskExecutorConfigBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Abstract basic task executor  based on {@link ExecutorService}
 */
abstract public class TaskExecutor {

    protected static final Logger logger = LoggerFactory.getLogger(TaskExecutor.class);

    protected final DefaultTaskPerformer defaultConsumer;

    abstract protected ExecutorService getExecutorService();

    abstract protected TaskExecutorConfigBase getConfig();

    public TaskExecutor(DefaultTaskPerformer defaultConsumer) {
        this.defaultConsumer = defaultConsumer;
    }

    protected final <P> Runnable createTask(final P taskPayload) {
        final long enqueueTime = System.currentTimeMillis();
        logger.debug("New task queued: {}", taskPayload);
        return () -> {
            if (logger.isDebugEnabled()) {
                final long queueingDuration = System.currentTimeMillis() - enqueueTime;
                logger.debug("Task ready to be executed. Duration being in queue {}", queueingDuration);
            }
            TaskPerformer performer = getConfig().getTaskPerformer();
            if (performer != null) {
                performer.perform(taskPayload);
            } else {
                defaultConsumer.perform(taskPayload);
            }
        };
    }

    public final <P> void submit(final P taskPayload) {
        getExecutorService().submit(createTask(taskPayload));
    }

    public final void stop() {
        getExecutorService().shutdown();
    }

    public final void awaitTermination(long seconds) {
        try {
            boolean taskCompleted = getExecutorService().awaitTermination(seconds, TimeUnit.SECONDS);
            if (!taskCompleted) {
                logger.info("Some tasks were not completed for payload: " + getConfig().getPayloadType().getSimpleName());
                final List<Runnable> rejected = getExecutorService().shutdownNow();
                logger.info("Rejected tasks: " + rejected.size());
            }
        } catch (InterruptedException e) {
            logger.error("Await termination interrupted");
        }
    }
}
