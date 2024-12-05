package colesico.framework.example.asynctask.eventbus;

import colesico.framework.asynctask.TaskQueueConfigPrototype;
import colesico.framework.config.Config;

@Config
public class TaskQueueConfig extends TaskQueueConfigPrototype {

    @Override
    public Class<?> getPayloadType() {
        return TaskPayload.class;
    }
}
