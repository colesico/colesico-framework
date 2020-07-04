package colesico.framework.example.asynctask;

import colesico.framework.asynctask.TaskSubmitter;
import colesico.framework.service.Service;

@Service
public class TaskSubmitterService {
    
    final TaskSubmitter taskSubmitter;

    public TaskSubmitterService(TaskSubmitter taskSubmitter) {
        this.taskSubmitter = taskSubmitter;
    }

    public void enqueueTask() {
        TaskPayload payload = new TaskPayload("value");
        taskSubmitter.submit(payload);
    }
}
