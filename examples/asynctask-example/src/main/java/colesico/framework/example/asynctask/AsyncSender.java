package colesico.framework.example.asynctask;

import colesico.framework.asynctask.TaskExecutor;
import colesico.framework.service.Service;

import javax.inject.Inject;
import java.util.Collection;
import java.util.concurrent.Future;

@Service
public class AsyncSender {

    final TaskExecutor executor;

    @Inject
    public AsyncSender(TaskExecutor dispatcher) {
        this.executor = dispatcher;
    }

    public Collection<Future<String>> produceTasks() {
        return executor.submitReturn(new Task2("Hello2"));
    }
}
