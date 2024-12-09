package colesico.framework.example.asynctask.eventbus;

import colesico.framework.eventbus.OnEvent;
import colesico.framework.service.Service;

/**
 * Task listener
 */
@Service
public class TaskListenerService {

    public TaskPayload payload;

    /**
     * This method performs task
     */
    @OnEvent
    public void performTask(TaskPayload payload) {
        this.payload = payload;
    }

}
