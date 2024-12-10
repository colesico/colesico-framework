package colesico.framework.example.asynctask.eventbus;

import colesico.framework.task.TaskExecutorConfigPrototype;
import colesico.framework.config.Config;

@Config
public class TaskQueueConfig extends TaskExecutorConfigPrototype {

    @Override
    public Class<?> getPayloadType() {
        return TaskPayload.class;
    }
}
