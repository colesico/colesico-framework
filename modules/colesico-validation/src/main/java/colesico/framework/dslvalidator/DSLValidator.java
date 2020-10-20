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

package colesico.framework.dslvalidator;

import colesico.framework.validation.ValidationException;
import colesico.framework.validation.ValidationIssue;
import colesico.framework.validation.Validator;

/**
 * Validator is used to validate a values with the validation program.
 *
 * @author Vladlen Larionov
 */
public final class DSLValidator<V> implements Validator<V> {

    /**
     * Root context subject
     */
    private final String subject;
    private final Command program;

    public DSLValidator(Command program, String subject) {
        this.subject = subject;
        this.program = program;
    }

    /**
     * Validates the value. Returns validation result.
     */
    @Override
    public ValidationIssue validate(V value, Object... params) {
        ValidationContext<V> context = ValidationContext.ofRoot(subject, value, params);
        program.execute(context);
        return context.toIssue();
    }

    /**
     * Validates the value. Throws exception if validation errors occurred
     *
     * @throws ValidationException if some validation errors occurred
     */
    @Override
    public void accept(V value, Object... params) throws ValidationException {
        ValidationContext<V> context = ValidationContext.ofRoot(subject, value, params);
        program.execute(context);
        ValidationIssue issue = context.toIssue();
        if (issue != null) {
            throw new ValidationException(issue);
        }
    }

}
