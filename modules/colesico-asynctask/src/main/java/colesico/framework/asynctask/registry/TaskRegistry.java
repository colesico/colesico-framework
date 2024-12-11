package colesico.framework.asynctask.registry;


import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

public interface TaskRegistry {

    boolean hasWorkers(Class<?> taskClass);

    <T, R> WorkersGroup<T, R> getTaskWorkers(Class<T> taskClass);

    Collection<Class<?>> getTaskTypes();

    <T, R, O> Collection<O> applyReturn(Class<T> taskClass, Function<TaskWorker<T, R>, O> action);

    <T, R> void applyVoid(Class<T> taskClass, Consumer<TaskWorker<T, R>> action);
}
