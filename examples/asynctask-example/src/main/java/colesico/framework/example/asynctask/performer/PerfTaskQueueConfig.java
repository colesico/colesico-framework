package colesico.framework.example.asynctask.performer;

import colesico.framework.asynctask.TaskPerformer;
import colesico.framework.task.TaskSubmitterConfigPrototype;
import colesico.framework.config.Config;

import javax.inject.Inject;

@Config
public class PerfTaskQueueConfig extends TaskSubmitterConfigPrototype {

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
