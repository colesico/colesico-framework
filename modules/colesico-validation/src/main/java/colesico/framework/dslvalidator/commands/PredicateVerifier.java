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

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.ValidationContext;
import colesico.framework.translation.Translatable;

import java.util.function.Predicate;

/**
 * @author Vladlen Larionov
 */
public final class PredicateVerifier<V> implements Command<V> {

    private final Predicate<ValidationContext<V>> predicate;
    private final String errorCode;
    private final Translatable errorMessage;
    private final Object[] messageParams;

    public PredicateVerifier(Predicate<ValidationContext<V>> predicate, String errorCode, Translatable errorMessage, Object... messageParam) {
        this.predicate = predicate;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.messageParams = messageParam;
    }

    @Override
    public void execute(ValidationContext<V> context) {
        if (!predicate.test(context)) {
            context.addError(errorCode, errorMessage.translate(messageParams));
        }
    }
}
