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

import java.util.function.Function;

/**
 * Extract nested value from current context value with mapper function
 * and applies command to  that nested value within new nested context.
 * <p>
 * If current context value is null provides null value to subcontext
 * without involving an mapper
 *
 * @author Vladlen Larionov
 */
public final class ValueMapper<V, N> extends Mapper<V, N> {

    /**
     * Extracts nested value from value
     */
    private final Function<V, N> mapper;

    public ValueMapper(String subject, Function<V, N> mapper, Command<N> command) {
        super(subject, command);
        this.mapper = mapper;
    }

    @Override
    public void execute(ValidationContext<V> context) {
        V currentValue = context.getValue();
        N nestedValue;
        if (currentValue != null) {
            nestedValue = mapper.apply(currentValue);
        } else {
            nestedValue = null;
        }
        ValidationContext<N> nestedContext = ValidationContext.ofNested(context, subject, nestedValue);
        command.execute(nestedContext);
    }
}
