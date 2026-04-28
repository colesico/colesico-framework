package colesico.framework.ioc.internal;

import colesico.framework.ioc.key.Key;
import colesico.framework.ioc.scope.Fabricator;
import colesico.framework.ioc.scope.RequestScope;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RequestScopeImpl implements RequestScope {

    /**
     * requestId -> objects map
     */
    private static final Map<UUID, Map<Key<?>, Object>> objectsHolder = new ConcurrentHashMap<>();
    private static final ThreadLocal<UUID> requestIdHolder = new ThreadLocal<>();

    private Map<Key<?>, Object> objectsHolder() {
        UUID id = requestIdHolder.get();
        if (id == null) throw new IllegalStateException("Scope not opened");
        Map<Key<?>, Object> map = objectsHolder.get(id);
        if (map == null) throw new IllegalStateException("Scope data already cleared");
        return map;
    }

    @Override
    public void open() {
        open(UUID.randomUUID());
    }

    @Override
    public void open(UUID requestId) {
        if (requestIdHolder.get() != null) {
            throw new IllegalStateException("Request scope already open in this thread");
        }
        requestIdHolder.set(requestId);
        objectsHolder.computeIfAbsent(requestId, k -> new ConcurrentHashMap<>());
    }

    @Override
    public <T> T get(Key<T> key) {
        return (T) objectsHolder().get(key);
    }

    @Override
    public <T> void put(Key<T> key, T value) {
        objectsHolder().put(key, value);
    }

    @Override
    public <T, C> T get(Key<T> key, Fabricator<T, C> fabricator, C fabricationContext) {
        Object obj = objectsHolder().computeIfAbsent(key, k -> fabricator.fabricate(fabricationContext));
        return (T) obj;
    }

    @Override
    public <T> void remove(Key<T> key) {
        objectsHolder().remove(key);
    }

    @Override
    public Set<Key<?>> keys() {
        return objectsHolder().keySet();
    }

    @Override
    public UUID requestId() {
        return requestIdHolder.get();
    }

    @Override
    public void close() {
        UUID requestId = requestIdHolder.get();
        if (requestId != null) {
            objectsHolder.remove(requestId);
            requestIdHolder.remove();
        }
    }
}
