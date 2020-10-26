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
import colesico.framework.dslvalidator.t9n.ValidatorMessages;

/**
 * If context value is not null executes all sequence commands within the current context.
 * Executes commands until first validation error occurs.
 * <p>
 * If current context value is null throws validation exception.
 *
 * @see GroupSequence
 */
public final class ChainSequence<V> extends AbstractSequence<V, V> {

    private final ValidatorMessages msg;

    public ChainSequence(ValidatorMessages msg) {
        this.msg = msg;
    }

    @Override
    public void execute(ValidationContext<V> context) {
        if (context.getValue() != null) {
            executeChain(context);
        } else {
            context.addError(OptionalGroupSequence.class.getSimpleName(), msg.mandatoryValueIsNull());
        }
    }
}
