package colesico.framework.example.asynctask.eventbus;

import colesico.framework.taskhub.EventSubmitterConfigPrototype;
import colesico.framework.config.Config;

@Config
public class TaskQueueConfig extends EventSubmitterConfigPrototype {

    @Override
    public Class<?> getPayloadType() {
        return TaskPayload.class;
    }
}
