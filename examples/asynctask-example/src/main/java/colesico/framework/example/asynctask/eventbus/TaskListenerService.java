package colesico.framework.example.asynctask.eventbus;

import colesico.framework.task.OnTask;
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
    @OnTask
    public void performTask(TaskPayload payload) {
        this.payload = payload;
    }

}
