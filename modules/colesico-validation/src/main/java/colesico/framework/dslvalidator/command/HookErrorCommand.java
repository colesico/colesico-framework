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
import colesico.framework.translation.Translatable;

/**
 * Checks for context nested errors presents n.
 * If nested errors is present adds error to current context
 */
public class HookErrorCommand<V> implements Command<V> {

    private final String errorCode;
    private final Translatable errorMessage;
    private final Object[] messageParams;

    public HookErrorCommand(String errorCode, Translatable errorMessage, Object... messageParam) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.messageParams = messageParam;
    }

    @Override
    public void execute(ValidationContext<V> context) {
        if (context.hasNestedErrors()) {
            context.addError(errorCode, errorMessage.translate(messageParams));
        }
    }
}
