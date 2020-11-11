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
import colesico.framework.translation.Translatable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This sequence checks for nested contexts errors presents after commands execution.
 * If nested exceptions is present adds error to current context
 *
 * @param
 */
public class HookErrorChain<V> extends AbstractSequence<V, V> {

    private final String errorCode;
    private final Translatable errorMessage;
    private final Object[] messageParams;

    public HookErrorChain(String errorCode, Translatable errorMessage, Object... messageParam) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.messageParams = messageParam;
    }

    protected boolean hasNestedErrors(ValidationContext<V> context) {
        Collection<ValidationContext> nestedContexts = context.getNestedContexts().values();
        for (ValidationContext nestedCtx : nestedContexts) {
            if (nestedCtx.hasErrors()) {
                return true;
            } else if (hasNestedErrors(nestedCtx)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute(ValidationContext<V> context) {
        executeChain(context);
        if (hasNestedErrors(context)) {
            context.addError(errorCode, errorMessage.translate(messageParams));
        }
    }
}
