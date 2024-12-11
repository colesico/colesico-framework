package colesico.framework.asynctask.internal;

import colesico.framework.asynctask.AbstractTaskExecutorConfig;
import colesico.framework.asynctask.TaskVTExecutor;
import colesico.framework.asynctask.TaskVTExecutorConfigPrototype;
import colesico.framework.asynctask.registry.TaskRegistry;

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
