package colesico.framework.example.asynctask;

import colesico.framework.asynctask.TaskSubmitter;
import colesico.framework.service.Service;

@Service
public class TaskPublisherService {
    final TaskSubmitter taskSubmitter;

    public TaskPublisherService(TaskSubmitter taskSubmitter) {
        this.taskSubmitter = taskSubmitter;
    }

    public void enqueueTask() {
        ATask task = new ATask("value");
        taskSubmitter.submit(task);
    }
}
