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

/**
 * Assumes that the current context value is iterable.
 * Applies commands to each iterated value within own sub context
 *
 * @param <V>
 */
public class IterableChain<V extends Iterable<I>, I> extends AbstractSequence<V, I> {

    @Override
    public void execute(ValidationContext<V> context) {
        Iterable<I> items = context.getValue();
        if (items == null) {
            return;
        }
        int idx = 0;
        for (I itemValue : items) {
            ValidationContext<I> nestedContext = ValidationContext.ofNested(context, Integer.toString(idx++), itemValue);
            executeChain(nestedContext);
        }
    }

}
