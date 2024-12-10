package colesico.framework.task.registry;


import java.util.Collection;

public interface TaskRegistry {

    boolean hasWorkers(Class<?> taskClass);

    <T,R> WorkersGroup<T,R> getTaskWorkers(Class<T> taskClass);

    Collection<Class<?>> getTaskTypes();
}
