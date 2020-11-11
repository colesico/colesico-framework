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

package colesico.framework.dslvalidator.commands;

import colesico.framework.dslvalidator.ValidationContext;

import java.util.Map;

/**
 * Perform command execution on a map entry  determined by key.
 *
 * @author Vladlen Larionov
 */
public final class MapChain<V extends Map<K, E>, K, E> extends AbstractSequence<V, E> {

    /**
     * Nested context subject
     */
    private final String subject;

    /**
     * Element index
     */
    private final K key;

    public MapChain(String subject, K key) {
        this.subject = subject;
        this.key = key;
    }

    @Override
    public void execute(ValidationContext<V> context) {
        E entryValue = context.getValue().get(key);
        ValidationContext<E> nestedContext = ValidationContext.ofNested(context, subject, entryValue);
        executeChain(nestedContext);
    }
}
