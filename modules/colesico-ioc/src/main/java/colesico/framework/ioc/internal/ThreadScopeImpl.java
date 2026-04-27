/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package colesico.framework.ioc.internal;

import colesico.framework.ioc.key.Key;
import colesico.framework.ioc.scope.Fabricator;
import colesico.framework.ioc.scope.ThreadScope;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Default process scope implementation
 *
 * @author Vladlen Larionov
 */
public final class ThreadScopeImpl implements ThreadScope {

    private final ThreadLocal<Map<Key<?>, Object>> objectsHolder;

    public ThreadScopeImpl() {
        objectsHolder = ThreadLocal.withInitial(HashMap::new);
    }

    @Override
    public <T> void remove(Key<T> key) {
        objectsHolder.get().remove(key);
    }

    @Override
    public Set<Key<?>> keys() {
        return objectsHolder.get().keySet();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T, C> T get(Key<T> key, Fabricator<T, C> fabricator, C fabricationContext) {
        var objects = objectsHolder.get();
        Object obj = objects.computeIfAbsent(key, k -> fabricator.fabricate(fabricationContext));
        return (T) obj;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Key<T> key) {
        return (T) objectsHolder.get().get(key);
    }

    @Override
    public <T> void put(Key<T> key, T value) {
        objectsHolder.get().put(key, value);
    }

    @Override
    public void init() {
        objectsHolder.get().clear();
    }

    @Override
    public void close() {
        objectsHolder.remove();
    }
}
