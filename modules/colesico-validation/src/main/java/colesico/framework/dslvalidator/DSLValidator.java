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

package colesico.framework.dslvalidator;

import colesico.framework.validation.ValidationException;
import colesico.framework.validation.ValidationIssue;
import colesico.framework.validation.Validator;

/**
 * Validation
 *
 * @author Vladlen Larionov
 */
public final class DSLValidator<V> implements Validator<V> {

    private final String rootSubject;
    private final Command rootCommand;

    public DSLValidator(String rootSubject, Command rootCommand) {
        this.rootSubject = rootSubject;
        this.rootCommand = rootCommand;
    }

    /**
     * Validates the value. Returns validation result.
     *
     * @param value
     * @return
     */
    @Override
    public ValidationIssue validate(V value) {
        ValidationContext<V> context = ValidationContext.ofRoot(rootSubject, value);
        rootCommand.execute(context);
        return context.toIssue();
    }

    /**
     * Validates the value. Throws exception if validation errors occurred
     *
     * @param value
     * @throws ValidationException if some validation errors occurred
     */
    @Override
    public void accept(V value) throws ValidationException {
        ValidationContext<V> context = ValidationContext.ofRoot(rootSubject, value);
        rootCommand.execute(context);
        ValidationIssue issue = context.toIssue();
        if (issue != null) {
            throw new ValidationException(issue);
        }
    }

}
