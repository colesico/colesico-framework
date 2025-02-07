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

import java.util.List;

/**
 * Perform command execution on a list element determined by index.
 * Assumes element value is null if the list value is null or the index is outside the list
 *
 * @author Vladlen Larionov
 */
public final class ItemMapper<V extends List<E>, E> extends Mapper<V, E> {

    /**
     * Element index
     */
    private final int index;

    public ItemMapper(String subject, int index, Command<E> command) {
        super(subject, command);
        this.index = index;
    }

    @Override
    public void execute(ValidationContext<V> context) {
        V currentValue = context.getValue();
        E elementValue;
        if (currentValue == null || index >= currentValue.size() || index < 0) {
            elementValue = null;
        } else {
            elementValue = currentValue.get(index);
        }
        ValidationContext<E> nestedContext = ValidationContext.ofNested(context, subject, elementValue);
        command.execute(nestedContext);
    }
}
