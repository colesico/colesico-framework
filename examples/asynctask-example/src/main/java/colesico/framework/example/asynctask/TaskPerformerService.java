package colesico.framework.example.asynctask;

import colesico.framework.eventbus.OnEvent;
import colesico.framework.service.Service;

@Service
public class TaskPerformerService {

    public ATask task;

    @OnEvent
    public void onTask(ATask task) {
        this.task = task;
    }
}
