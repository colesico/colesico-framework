package colesico.framework.eventbus.registry;


import java.util.Collection;

public interface EventRegistry {

    boolean hasListeners(Class<?> eventClass);

    <E> ListenersGroup<E> getEventListeners(Class<E> eventClass);

    Collection<Class<?>> getEventTypes();
}
