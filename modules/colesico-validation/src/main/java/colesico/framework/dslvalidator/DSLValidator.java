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

package colesico.framework.dslvalidator;

import colesico.framework.validation.ValidationException;
import colesico.framework.validation.ValidationIssue;
import colesico.framework.validation.Validator;

/**
 * Validator is used to  validate a values with the validation program.
 *
 * @author Vladlen Larionov
 */
public final class DSLValidator<V> implements Validator<V> {

    /**
     * Root context subject
     */
    private final String subject;

    /**
     * Start validation command
     */
    private final Command<V> validation;

    /**
     * Constructor
     *
     * @param subject    root validation subject
     * @param validation start validation command
     */
    public DSLValidator(String subject, Command<V> validation) {
        this.subject = subject;
        this.validation = validation;
    }

    public DSLValidator(Command<V> validation) {
        this.validation = validation;
        this.subject = null;
    }

    /**
     * Validates the value. Returns validation result.
     */
    @Override
    public ValidationIssue validate(V value, Object... params) {
        ValidationContext<V> context = ValidationContext.ofRoot(subject, value, params);
        validation.execute(context);
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
        validation.execute(context);
        ValidationIssue issue = context.toIssue();
        if (issue != null) {
            throw new ValidationException(issue);
        }
    }

    public Command<V> getValidation() {
        return validation;
    }

    public String getSubject() {
        return subject;
    }
}
