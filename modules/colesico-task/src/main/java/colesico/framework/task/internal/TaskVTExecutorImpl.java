package colesico.framework.task.internal;

import colesico.framework.task.AbstractTaskExecutorConfig;
import colesico.framework.task.TaskVTExecutor;
import colesico.framework.task.TaskVTExecutorConfigPrototype;
import colesico.framework.task.registry.TaskRegistry;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Task queue
 */
@Singleton
public final class TaskVTExecutorImpl extends AbstractTaskExecutor implements TaskVTExecutor {

    private final TaskVTExecutorConfigPrototype config;

    private final ExecutorService executorService;

    @Inject
    public TaskVTExecutorImpl(TaskRegistry taskRegistry, TaskVTExecutorConfigPrototype config) {
        super(taskRegistry);
        this.config = config;
        executorService = Executors.newVirtualThreadPerTaskExecutor();
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
