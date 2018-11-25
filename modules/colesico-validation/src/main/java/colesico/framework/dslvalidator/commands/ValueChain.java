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
 * Extract subvalue from current context value with valueExtractor function
 * and applies chain commands to that nested value within new subcontext.
 *
 * @author Vladlen Larionov
 */
public final class ValueChain<V, S> extends AbstractChain<V> {

    private final String subject;
    private final Function<V, S> valueExtractor;

    public ValueChain(String subject, Function<V, S> valueExtractor) {
        this.subject = subject;
        this.valueExtractor = valueExtractor;
    }

    @Override
    public void execute(ValidationContext<V> context) {
        S childValue = valueExtractor.apply(context.getValue());
        ValidationContext childContext = ValidationContext.ofChild(context, subject, childValue);
        executeCommands(childContext);
    }
}
