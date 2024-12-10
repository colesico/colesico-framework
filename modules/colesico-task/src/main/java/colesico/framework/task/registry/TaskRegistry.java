package colesico.framework.task.registry;


import java.util.Collection;
import java.util.function.Function;

public interface TaskRegistry {

    boolean hasWorkers(Class<?> taskClass);

    <T, R> WorkersGroup<T, R> getTaskWorkers(Class<T> taskClass);

    Collection<Class<?>> getTaskTypes();

    <T, R, O> Collection<O> apply(Class<T> taskClass, Function<TaskWorker<T, R>, O> action);
}
