package colesico.framework.example.asynctask.performer;

import colesico.framework.asynctask.TaskPerformer;
import colesico.framework.config.Config;

import javax.inject.Inject;

@Config
public class PerfTaskQueueConfig extends colesico.framework.asynctask.TaskQueueConfigPrototype {

    private final TaskPerformerService performer;

    @Inject
    public PerfTaskQueueConfig(TaskPerformerService performer) {
        this.performer = performer;
    }

    @Override
    public Class<?> getPayloadType() {
        return PerfTaskPayload.class;
    }

    @Override
    public TaskPerformer getTaskPerformer() {
        return performer;
    }
}
