package colesico.framework.ioc.internal;

import colesico.framework.ioc.key.Key;
import colesico.framework.ioc.scope.Fabricator;
import colesico.framework.ioc.scope.TaskScope;
import jakarta.inject.Singleton;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public final class TaskScopeImpl implements TaskScope {

    /**
     * The ScopedValue that holds the current scope's object map for this instance.
     * Each TaskScope instance has its own binding key.
     */
    private final ScopedValue<Map<Key<?>, Object>> dataHolder = ScopedValue.newInstance();

    @Override
    public void run(Runnable task) {
        if (dataHolder.isBound()) {
            throw new IllegalStateException("A task scope is already active");
        }
        ScopedValue.where(dataHolder, new ConcurrentHashMap<>()).run(task);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Key<T> key) {
        return (T) currentData().get(key);
    }

    @Override
    public <T> void put(Key<T> key, T value) {
        currentData().put(key, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T, C> T get(Key<T> key, Fabricator<T, C> fabricator, C fabricationContext) {
        var data = currentData();
        Object obj = data.computeIfAbsent(key, k -> fabricator.fabricate(fabricationContext));
        return (T) obj;
    }

    @Override
    public <T> void remove(Key<T> key) {
        currentData().remove(key);
    }

    @Override
    public Set<Key<?>> keys() {
        return new HashSet<>(currentData().keySet());
    }

    @Override
    public void clear() {
        currentData().clear();
    }

    /**
     * Helper to retrieve the current map from ScopedValue
     */
    private Map<Key<?>, Object> currentData() {
        if (!dataHolder.isBound()) {
            throw new IllegalStateException("No active task scope. Wrap the task execution with TaskScope.run(...)");
        }
        return dataHolder.get();
    }
}

