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
import colesico.framework.dslvalidator.ValidationContext;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

import static colesico.framework.assist.StringUtils.isBlank;

/**
 * Executes chain command if beginValue ins not null and not blank.
 * Add error to  processorContext if beginValue is null or blank.
 *
 * @author Vladlen Larionov
 */
public final class RequiredVerifier<V> implements Command<V> {

    private final ValidatorMessages msg;

    public RequiredVerifier(ValidatorMessages msg) {
        this.msg = msg;
    }

    private void addError(ValidationContext<V> context) {
        context.addError(RequiredVerifier.class.getSimpleName(), msg.valueRequired());
    }

    @Override
    public void execute(ValidationContext<V> context) {
        if (context.value() == null) {
            addError(context);
            return;
        }

        if (context.value() instanceof String) {
            if (isBlank((String) context.value())) {
                addError(context);
            }
            return;
        }

        if (context.value() instanceof Collection) {
            if (((Collection) context.value()).isEmpty()) {
                addError(context);
            }
            return;
        }

        if (context.value() instanceof Map) {
            if (((Map) context.value()).isEmpty()) {
                addError(context);
            }
            return;
        }

        if (context.value().getClass().isArray()) {
            if (Array.getLength(context.value()) == 0) {
                addError(context);
            }
        }
    }
}
