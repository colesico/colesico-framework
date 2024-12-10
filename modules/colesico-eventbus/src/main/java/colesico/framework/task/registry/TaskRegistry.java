package colesico.framework.task.registry;


import java.util.Collection;

public interface TaskRegistry {

    boolean hasListeners(Class<?> taskClass);

    <E> ListenersGroup<E> getTaskListeners(Class<E> taskClass);

    Collection<Class<?>> getTaskTypes();
}
