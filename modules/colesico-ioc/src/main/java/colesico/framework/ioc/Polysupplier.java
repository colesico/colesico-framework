/*
 * Copyright © 2014-2020 Vladlen V. Larionov and others as noted.
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

package colesico.framework.ioc;

import colesico.framework.ioc.annotation.Polyproduce;
import colesico.framework.ioc.key.Key;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Ioc container supports multiple factories for a given key.
 * Polysupplier represents a chain of factories for the given key.
 *
 * @param <T> instance type
 * @see Key < T >
 * @see Polyproduce
 */
public interface Polysupplier<T> {
    /**
     * True if this polysupplier can supply instances
     *
     * @return true is polysupplier is empty
     */
    boolean isNotEmpty();

    /**
     * Returns iterator to get the instances from the factories chain
     *
     * @param message message to be passed
     * @return iterator
     */
    Iterator<T> iterator(Object message);

    default void forEach(Consumer<? super T> action, Object message) {
        if (isNotEmpty()) {
            Iterator<T> it = iterator(message);
            while (it.hasNext()) {
                action.accept(it.next());
            }
        }
    }
}
