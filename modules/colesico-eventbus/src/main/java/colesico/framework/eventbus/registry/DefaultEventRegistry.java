package colesico.framework.eventbus.registry;

import colesico.framework.ioc.production.Polysupplier;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DefaultEventRegistry implements EventRegistry {

    protected final Map<Class<?>, ListenersGroup<?>> eventListeners = new HashMap<>();

    @Inject
    public DefaultEventRegistry(Polysupplier<ServiceListener> listenersSupp) {
        initEventListeners(listenersSupp);
    }

    @SuppressWarnings("unchecked")
    protected void initEventListeners(Polysupplier<ServiceListener> listenersSupp) {
        listenersSupp.forEach(
                listener -> {
                    var bindings = listener.getEventBindings();
                    for (var binding : bindings) {
                        var listeners = eventListeners.computeIfAbsent(binding.eventClass(), c -> new ListenersGroup<>());
                        listeners.add(binding.listener());
                    }
                }, null
        );
    }

    @Override
    public boolean hasListeners(Class<?> eventClass) {
        return eventListeners.containsKey(eventClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> ListenersGroup<E> getEventListeners(Class<E> eventClass) {
        return (ListenersGroup<E>) eventListeners.get(eventClass);
    }

    @Override
    public Collection<Class<?>> getEventTypes() {
        return eventListeners.keySet();
    }
}
