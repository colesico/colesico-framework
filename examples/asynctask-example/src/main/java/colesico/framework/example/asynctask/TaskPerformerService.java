package colesico.framework.example.asynctask;

import colesico.framework.eventbus.OnEvent;
import colesico.framework.service.Service;

@Service
public class TaskPerformerService {

    public TaskPayload payload;

    /**
     * This method performs task
     */
    @OnEvent
    public void performTask(TaskPayload payload) {
        this.payload = payload;
    }
}
