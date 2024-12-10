package colesico.framework.task.registry;

import colesico.framework.ioc.production.Polysupplier;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DefaultTaskRegistry implements TaskRegistry {

    protected final Map<Class<?>, ListenersGroup<?,?>> taskListeners = new HashMap<>();

    @Inject
    public DefaultTaskRegistry(Polysupplier<ServiceListener> listenersSupp) {
        initTaskListeners(listenersSupp);
    }

    @SuppressWarnings("unchecked")
    protected void initTaskListeners(Polysupplier<ServiceListener> listenersSupp) {
        listenersSupp.forEach(
                listener -> {
                    var bindings = listener.getTaskBindings();
                    for (var binding : bindings) {
                        var listeners = taskListeners.computeIfAbsent(binding.taskClass(), c -> new ListenersGroup<>());
                        listeners.add(binding.listener());
                    }
                }, null
        );
    }

    @Override
    public boolean hasListeners(Class<?> taskClass) {
        return taskListeners.containsKey(taskClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> ListenersGroup<E> getTaskListeners(Class<E> taskClass) {
        return (ListenersGroup<E>) taskListeners.get(taskClass);
    }

    @Override
    public Collection<Class<?>> getTaskTypes() {
        return taskListeners.keySet();
    }
}
