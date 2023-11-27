/*
 * Copyright © 2014-2023 Vladlen V. Larionov and others as noted.
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

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.key.Key;
import colesico.framework.ioc.key.TypeKey;
import colesico.framework.ioc.scope.Fabricator;
import colesico.framework.ioc.scope.RefreshScope;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default process scope implementation
 *
 * @author Vladlen Larionov
 */
public final class RefreshScopeImpl implements RefreshScope {

    private final Map<Key<?>, Object> beans;
    private final Ioc ioc;

    public RefreshScopeImpl(Ioc ioc) {
        beans = new ConcurrentHashMap();
        this.ioc = ioc;
    }

    @Override
    public <T,M> T refresh(Key<T> key, M message) {
        T bean = (T) beans.remove(key);
        if (bean != null) {
            return ioc.instance(key, message);
        }
        return null;
    }

    @Override
    public <T> T refresh(Class<T> type) {
        return refresh(new TypeKey<>(type),null);
    }

    @Override
    public void refreshAll() {
        for (Key<?> key : beans.keySet()) {
            remove(key);
        }
    }


    @Override
    public <T> void remove(Key<T> key) {
        beans.remove(key);
    }

    @Override
    public Set<Key<?>> getKeys() {
        return beans.keySet();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T, C> T get(Key<T> key, Fabricator<T, C> fabricator, C fabricationContext) {
        Object obj = beans.computeIfAbsent(key, k -> fabricator.fabricate(fabricationContext));
        return (T) obj;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Key<T> key) {
        return (T) beans.get(key);
    }

    @Override
    public <T> void put(Key<T> key, T value) {
        beans.put(key, value);
    }

}
