package colesico.framework.asynctask.internal;

import colesico.framework.asynctask.AbstractTaskExecutorConfig;
import colesico.framework.asynctask.registry.TaskRegistry;
import colesico.framework.asynctask.registry.TaskWorker;
import colesico.framework.asynctask.registry.WorkersGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Abstract basic task executor  based on {@link ExecutorService}
 */
abstract public class AbstractTaskExecutor {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractTaskExecutor.class);

    protected final TaskRegistry registry;

    private volatile boolean running = false;

    abstract protected ExecutorService getExecutorService();

    abstract protected AbstractTaskExecutorConfig getConfig();

    public AbstractTaskExecutor(TaskRegistry registry) {
        this.registry = registry;
    }

    protected void checkRunning() {
        if (!running) {
            throw new IllegalStateException("Async tasks service is not started");
        }
    }

    protected final <T, R> Callable<R> createCallableTask(final TaskWorker<T, R> worker, final T task) {
        logger.debug("New callable task: {}", task);
        return () -> {
            logger.debug("Execute callable task: {}", task);
            return worker.work(task);
        };
    }

    protected final <T> Runnable createRunnableTask(final TaskWorker<T, ?> worker, final T task) {
        logger.debug("New runnable task: {}", task);
        return () -> {
            logger.debug("Execute runnable task: {}", task);
            worker.work(task);
        };
    }

    public <T, R> void dispatchVoid(final T task) {
        var workers = (WorkersGroup<T, R>) registry.getTaskWorkers(task.getClass());
        if (workers != null) {
            workers.applyVoid(worker -> worker.work(task));
        }
    }

    public <T, R> Collection<R> dispatchReturn(final T task) {
        var workers = (WorkersGroup<T, R>) registry.getTaskWorkers(task.getClass());
        if (workers != null) {
            return workers.applyReturn(worker -> worker.work(task));
        }
        return List.of();
    }

    public <T> void submitVoid(final T task) {
        checkRunning();
        registry.applyVoid(task.getClass(),
                worker -> getExecutorService().execute(createRunnableTask((TaskWorker<T, ?>) worker, task))
        );
    }

    public <T, R> Collection<Future<R>> submitReturn(final T task) {
        checkRunning();
        return registry.applyReturn(task.getClass(),
                worker -> getExecutorService().submit(createCallableTask((TaskWorker<T, R>) worker, task))
        );
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
            boolean taskCompleted = getExecutorService().awaitTermination(getConfig().getAwaitTerminationSeconds(), TimeUnit.SECONDS);
            if (!taskCompleted) {
                logger.info("Some tasks were not completed for task executor");
                final List<Runnable> rejected = getExecutorService().shutdownNow();
                logger.info("Rejected tasks: " + rejected.size());
            }
        } catch (InterruptedException t) {
            logger.error("Await termination interrupted");
        } catch (Exception t) {
            logger.error("Error stopping task executor: {}", t.getMessage());
        }
        running = false;
    }

    public boolean isRunning() {
        return running;
    }
}
