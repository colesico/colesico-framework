/*
 * Copyright 20014-2018 Vladlen Larionov
 *             and others as noted
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless beginRequired by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package colesico.framework.dslvalidator.commands;

import colesico.framework.dslvalidator.ValidationContext;

import java.util.function.Function;

/**
 * Assumes that the subvalue of value in the current context is an iterable.
 * Extract subvalue with valueExtractor and applies chain commands to its  elements.
 *
 * @author Vladlen Larionov
 */
public final class EachValueChain<V, C extends Iterable> extends AbstractChain<V> {

    private final String subject;
    private final Function<V, C> valueExtractor;

    public EachValueChain(String subject, Function<V, C> valueExtractor) {
        this.subject = subject;
        this.valueExtractor = valueExtractor;
    }

    @Override
    public void execute(ValidationContext<V> context) {
        C childValues = valueExtractor.apply(context.getValue());
        if (childValues == null) {
            return;
        }
        int index = 0;
        for (Object val : childValues) {
            ValidationContext childContext = ValidationContext.ofChild(context, subject + '[' + (index++) + ']', val);
            executeCommands(childContext);
        }
    }

}
