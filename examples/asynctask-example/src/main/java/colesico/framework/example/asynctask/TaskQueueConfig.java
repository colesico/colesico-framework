package colesico.framework.example.asynctask;

import colesico.framework.asynctask.TaskQueueConfigPrototype;
import colesico.framework.config.Config;

@Config
public class TaskQueueConfig extends TaskQueueConfigPrototype {

    @Override
    public Class<?> getPayloadType() {
        return ATask.class;
    }
}
