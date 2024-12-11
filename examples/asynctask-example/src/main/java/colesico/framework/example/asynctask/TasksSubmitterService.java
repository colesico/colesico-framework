package colesico.framework.example.asynctask;

import colesico.framework.asynctask.TaskExecutor;
import colesico.framework.example.asynctask.eventbus.TaskPayload;
import colesico.framework.example.asynctask.performer.PerfTaskPayload;
import colesico.framework.service.Service;

@Service
public class TasksSubmitterService {

    final TaskExecutor taskSubmitter;

    public TasksSubmitterService(TaskExecutor taskSubmitter) {
        this.taskSubmitter = taskSubmitter;
    }

    public void enqueueTasks() {
        // Submit first task
        TaskPayload payload = new TaskPayload("value");
        taskSubmitter.submitReturn(payload);

        // submit second task
        PerfTaskPayload perfPayload = new PerfTaskPayload("perf");
        taskSubmitter.submitReturn(perfPayload);
    }
}
