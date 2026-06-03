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
package colesico.framework.ioc.scope;

import colesico.framework.ioc.key.Key;

import java.util.Set;

/**
 * Scope interface.
 * All custom scopes for the IoC container must implement this interface.
 *
 * @author Vladlen Larionov
 */
public interface Scope {

    /**
     * Returns an object from the scope by its key. If object is not exists returns null
     */
    <T> T get(Key<T> key);

    /**
     * Puts an object to the scope and associate it with key
     */
    <T> void put(Key<T> key, T value);

    /**
     * Returns an object from the scope by key.
     * If the object is not present in the scope, creates it with  the fabricator,
     * puts to scope and returns this object
     */
    <T,C> T get(Key<T> key, Fabricator<T,C> fabricator, C fabricationContext);

    /**
     * Removes an object from the scope
     */
    <T> void remove(Key<T> key);

    /**
     * Returns the scope keys
     */
    Set<Key<?>> keys();

    /**
     * Clear all scope data
     */
    void clear();
}
