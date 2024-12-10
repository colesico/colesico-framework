package colesico.framework.example.asynctask.eventbus;

import colesico.framework.task.TaskSubmitterConfigPrototype;
import colesico.framework.config.Config;

@Config
public class TaskQueueConfig extends TaskSubmitterConfigPrototype {

    @Override
    public Class<?> getPayloadType() {
        return TaskPayload.class;
    }
}
