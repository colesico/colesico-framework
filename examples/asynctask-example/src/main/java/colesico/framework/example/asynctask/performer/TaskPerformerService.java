package colesico.framework.example.asynctask.performer;

import colesico.framework.asynctask.TaskPerformer;
import colesico.framework.service.Service;

/**
 * Task performer
 */
@Service
public class TaskPerformerService implements TaskPerformer<PerfTaskPayload> {

    public PerfTaskPayload payload;

    @Override
    public void perform(PerfTaskPayload payload) {
        this.payload = payload;
    }
}
