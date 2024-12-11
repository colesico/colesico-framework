package colesico.framework.example.asynctask;

import colesico.framework.asynctask.TaskExecutor;
import colesico.framework.service.Service;

import javax.inject.Inject;
import java.util.Collection;
import java.util.concurrent.Future;

@Service
public class AsyncProducerService {

    final TaskExecutor executor;

    @Inject
    public AsyncProducerService(TaskExecutor dispatcher) {
        this.executor = dispatcher;
    }

    public Collection<Future<String>> produceTasks() {
        Collection<Future<String>> res = executor.submitReturn(new Task1("Hello1"));
        executor.submit(new Task2("Hello2"));
        return res;
    }
}
