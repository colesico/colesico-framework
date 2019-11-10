/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.dslvalidator.commands;

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.ValidationContext;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * Executes chain command if beginValue ins not null and not blank.
 * Add error to processorContext if beginValue is null or blank.
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
        if (context.getValue() == null) {
            addError(context);
            return;
        }

        if (context.getValue() instanceof String) {
            if (StringUtils.isBlank((String) context.getValue())) {
                addError(context);
            }
            return;
        }

        if (context.getValue() instanceof Collection) {
            if (((Collection) context.getValue()).isEmpty()) {
                if (StringUtils.isBlank((String) context.getValue())) {
                    addError(context);
                }
            }
            return;
        }

        if (context.getValue() instanceof Map) {
            if (((Map) context.getValue()).isEmpty()) {
                if (StringUtils.isBlank((String) context.getValue())) {
                    addError(context);
                }
            }
            return;
        }

        if (context.getValue().getClass().isArray()) {
            if (((Object[]) context.getValue()).length == 0) {
                addError(context);
            }
        }
    }
}
