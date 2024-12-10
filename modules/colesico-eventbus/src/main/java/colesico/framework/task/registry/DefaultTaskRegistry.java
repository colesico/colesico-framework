package colesico.framework.task.registry;

import colesico.framework.ioc.production.Polysupplier;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Singleton
public class DefaultTaskRegistry implements TaskRegistry {

    protected final Map<Class<?>, WorkersGroup<?, ?>> taskWorkers = new HashMap<>();

    @Inject
    public DefaultTaskRegistry(Polysupplier<ServiceWorkers> workersSupp) {
        initTaskWorkers(workersSupp);
    }

    protected void initTaskWorkers(Polysupplier<ServiceWorkers> workersSupp) {
        workersSupp.forEach(
                worker -> {
                    var bindings = worker.getTaskBindings();
                    for (var binding : bindings) {
                        var workers = taskWorkers.computeIfAbsent(binding.taskClass(), c -> new WorkersGroup<>());
                        workers.add(binding.worker());
                    }
                }, null
        );
    }

    @Override
    public boolean hasWorkers(Class<?> taskClass) {
        return taskWorkers.containsKey(taskClass);
    }

    @Override
    public <T, R> WorkersGroup<T, R> getTaskWorkers(Class<T> taskClass) {
        return (WorkersGroup<T, R>) taskWorkers.get(taskClass);
    }

    @Override
    public Collection<Class<?>> getTaskTypes() {
        return taskWorkers.keySet();
    }

    @Override
    public <T, R, O> Collection<O> apply(Class<T> taskClass, Function<TaskWorker<T, R>, O> action) {
        var workers = (WorkersGroup<T, R>) getTaskWorkers(taskClass);
        if (workers != null) {
            return workers.apply(action);
        }
        return List.of();
    }
}
