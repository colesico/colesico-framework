/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to  in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.dslvalidator.command;

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.Mapper;
import colesico.framework.dslvalidator.ValidationContext;

import java.util.Map;

/**
 * Perform command execution on a map entry  determined by key.
 * Assumes entry value is null if the map value is null .
 *
 * @author Vladlen Larionov
 */
public final class EntryMapper<V extends Map<K, E>, K, E> extends Mapper<V, E> {

    /**
     * Map key
     */
    private final K key;

    public EntryMapper(String subject, K key, Command<E> command) {
        super(subject, command);
        this.key = key;
    }

    @Override
    public void execute(ValidationContext<V> context) {
        V currentValue = context.getValue();
        E entryValue;
        if (currentValue == null) {
            entryValue = null;
        } else {
            entryValue = currentValue.get(key);
        }
        ValidationContext<E> nestedContext = ValidationContext.ofNested(context, subject, entryValue);
        command.execute(nestedContext);
    }
}
