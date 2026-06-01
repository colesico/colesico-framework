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

package colesico.framework.http;

import java.util.*;

/**
 * Represents http values map
 */
public final class HttpValues<K, V> {

    private final Map<K, List<V>> valuesMap;

    public HttpValues(Map<K, List<V>> valuesMap) {
        this.valuesMap = valuesMap;
    }

    public boolean hasKey(K key) {
        return valuesMap.containsKey(key);
    }

    /**
     * Returns first value associated with given key or null.
     * To retrieve all values associated with given key use {@link HttpValues#getAll(K)}
     */
    public V get(K key) {
        List<V> multiValue = valuesMap.get(key);
        if (multiValue == null) {
            return null;
        }
        return multiValue.getFirst();
    }

    public Set<K> keys() {
        return Collections.unmodifiableSet(valuesMap.keySet());
    }

    /**
     * Returns all values associated with given key or null.
     */
    public List<V> getAll(K key) {
        return valuesMap.get(key);
    }

    public int size() {
        return valuesMap.size();
    }

    public boolean isEmpty() {
        return valuesMap.isEmpty();
    }

    public Map<K, List<V>> export() {
        return Collections.unmodifiableMap(valuesMap);
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder<>();
    }

    public static class Builder<K, V> {
        private final Map<K, List<V>> valuesMap = new HashMap<>();

        public void add(K key, V value) {
            var multiValue = valuesMap.computeIfAbsent(key, k -> new ArrayList<>());
            multiValue.add(value);
        }

        public HttpValues<K, V> build() {
            return new HttpValues<>(valuesMap);
        }
    }
}
