/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package colesico.framework.ioc.internal;

import colesico.framework.ioc.Fabricator;
import colesico.framework.ioc.Key;
import colesico.framework.ioc.ThreadScope;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Default process scope implementation
 *
 * @author Vladlen Larionov
 */
public final class ThreadScopeImpl implements ThreadScope {

    protected final ThreadLocal<Map<Key<?>, Object>> attributesHolder;

    public ThreadScopeImpl() {
        attributesHolder = ThreadLocal.withInitial(HashMap::new);
    }

    @Override
    public <T> void remove(Key<T> key) {
        attributesHolder.get().remove(key);
    }

    @Override
    public Set<Key<?>> getKeys() {
        return attributesHolder.get().keySet();
    }

    @Override
    public <T, C> T get(Key<T> key, Fabricator<T, C> fabricator, C fabricationContext) {
        Map attributes = attributesHolder.get();
        Object obj = attributes.computeIfAbsent(key, k -> fabricator.fabricate(fabricationContext));
        return (T) obj;
    }

    @Override
    public <T> T get(Key<T> key) {
        return (T) attributesHolder.get().get(key);
    }

    @Override
    public <T> void put(Key<T> key, T value) {
        attributesHolder.get().put(key, value);
    }

    @Override
    public void init() {
        attributesHolder.get().clear();
    }

    @Override
    public void destroy() {
        attributesHolder.remove();
    }

}
