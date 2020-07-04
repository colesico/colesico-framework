package colesico.framework.example.asynctask;

import colesico.framework.config.Config;

@Config
public class TaskQueueConfig extends colesico.framework.asynctask.TaskQueueConfigPrototype {

    @Override
    public Class<?> getPayloadType() {
        return TaskPayload.class;
    }
}
