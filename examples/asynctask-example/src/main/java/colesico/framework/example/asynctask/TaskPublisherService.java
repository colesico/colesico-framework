package colesico.framework.example.asynctask;

import colesico.framework.asynctask.TaskPublisher;
import colesico.framework.service.Service;

@Service
public class TaskPublisherService {
    final TaskPublisher taskPublisher;

    public TaskPublisherService(TaskPublisher taskPublisher) {
        this.taskPublisher = taskPublisher;
    }

    public void enqueueTask() {
        ATask task = new ATask("value");
        taskPublisher.enqueue(task);
    }
}
