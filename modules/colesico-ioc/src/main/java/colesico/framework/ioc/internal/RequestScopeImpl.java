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


    @Override
    public void init() {
        init(UUID.randomUUID());
    }

    @Override
    public void init(UUID requestId) {
        requestIdHolder.set(requestId);
        objectsHolder.computeIfAbsent(requestId, k -> new ConcurrentHashMap<>());
    }

    @Override
    public void destroy() {
        objectsHolder.remove(requestId());
        requestIdHolder.remove();
    }

    @Override
    public <T> T get(Key<T> key) {
        return (T) objectsHolder.get(requestId()).get(key);
    }

    @Override
    public <T> void put(Key<T> key, T value) {
        objectsHolder.get(requestId()).put(key, value);
    }

    @Override
    public <T, C> T get(Key<T> key, Fabricator<T, C> fabricator, C fabricationContext) {
        var objects = objectsHolder.get(requestId());
        Object obj = objects.computeIfAbsent(key, k -> fabricator.fabricate(fabricationContext));
        return (T) obj;
    }

    @Override
    public <T> void remove(Key<T> key) {
        objectsHolder.get(requestId()).remove(key);
    }

    @Override
    public Set<Key<?>> keys() {
        return objectsHolder.get(requestId()).keySet();
    }

    @Override
    public UUID requestId() {
        return requestIdHolder.get();
    }
}
