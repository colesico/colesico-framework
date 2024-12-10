package colesico.framework.task.internal;

import colesico.framework.task.TaskExecutorConfigPrototype;
import colesico.framework.task.registry.TaskHandler;
import colesico.framework.task.registry.TaskRegistry;
import colesico.framework.task.registry.ListenersGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Abstract basic task executor  based on {@link ExecutorService}
 */
abstract public class AbstractTaskExecutor {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractTaskExecutor.class);

    protected final TaskRegistry registry;

    private volatile boolean running = false;

    abstract protected ExecutorService getExecutorService();

    abstract protected TaskExecutorConfigPrototype getConfig();

    public AbstractTaskExecutor(TaskRegistry registry) {
        this.registry = registry;
    }

    public boolean isRunning() {
        return running;
    }

    private void checkRunning() {
        if (!running) {
            throw new IllegalStateException("Async tasks service is not started");
        }
    }

    protected final <E, R> Callable<R> createTask(final TaskHandler<E, R> listener, final E task) {
        final long enqueueTime = System.currentTimeMillis();
        logger.debug("New task queued: {}", task);
        return () -> {
            if (logger.isDebugEnabled()) {
                final long queueingDuration = System.currentTimeMillis() - enqueueTime;
                logger.debug("Task ready to be executed. Duration being in queue {}", queueingDuration);
            }
            return listener.handle(task);
        };
    }

    public <E> void submit(final E task) {
        checkRunning();
        var listeners = (ListenersGroup<E>) registry.getTaskListeners(task.getClass());
        if (listeners != null) {
            listeners.apply(listener -> getExecutorService().submit(createTask(listener, task)));
        }
    }

    public synchronized void start() {
        logger.debug("Starting  '{}' task executor...", this.getClass().getSimpleName());
        if (running) {
            throw new IllegalStateException("Task executor is already started");
        }
        running = true;
        logger.debug("Task executor has been started");
    }

    public synchronized void stop() {
        logger.debug("Stopping '{}' task executor...", this.getClass().getSimpleName());
        checkRunning();
        // Stop
        getExecutorService().shutdown();

        // Await termination
        try {
            getExecutorService().awaitTermination(getConfig().awaitTerminationSeconds(), TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("Error stopping task executor: {}", e.getMessage());
        }
        running = false;
    }


    public final void awaitTermination(long seconds) {
        try {
            boolean taskCompleted = getExecutorService().awaitTermination(seconds, TimeUnit.SECONDS);
            if (!taskCompleted) {
                logger.info("Some tasks were not completed for task executor");
                final List<Runnable> rejected = getExecutorService().shutdownNow();
                logger.info("Rejected tasks: " + rejected.size());
            }
        } catch (InterruptedException e) {
            logger.error("Await termination interrupted");
        }
    }
}
