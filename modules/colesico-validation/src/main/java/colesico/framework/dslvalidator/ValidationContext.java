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

import colesico.framework.validation.ValidationError;
import colesico.framework.validation.ValidationIssue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Validation context
 *
 * @author Vladlen Larionov
 */
public final class ValidationContext<V> {

    // Name of value
    private final String subject;

    // Value to be validated
    private final V value;

    // Validation errors
    private final List<ValidationError> errors = new ArrayList<>();

    // Reference to parent context
    private final ValidationContext<?> superContext;

    // Nested contexts ref.
    private final List<ValidationContext<?>> nestedContexts = new LinkedList<>();

    private ValidationContext(ValidationContext<?> superContext, String subject, V value) {
        this.subject = subject;
        this.value = value;
        this.superContext = superContext;
    }

    /**
     * Produce Root ValidationContext
     *
     * @param value
     * @param <V>
     * @return
     */
    public static <V> ValidationContext<V> ofRoot(String subject, V value) {
        return new ValidationContext(null, subject, value);
    }

    /**
     * Produce nested ValidationContext
     *
     * @param superContext
     * @param subject
     * @param value
     * @param <V>
     * @return
     */
    public static <V> ValidationContext<V> ofNested(ValidationContext<?> superContext, String subject, V value) {
        ValidationContext<V> childContext = new ValidationContext(superContext, subject, value);
        superContext.getNestedContexts().add(childContext);
        return childContext;
    }

    public String getSubject() {
        return subject;
    }

    public V getValue() {
        return value;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public void addError(String code, String message) {
        ValidationError error = new ValidationError(code, message);
        errors.add(error);
    }

    public List<ValidationContext<?>> getNestedContexts() {
        return nestedContexts;
    }

    public ValidationContext<?> getSuperContext() {
        return superContext;
    }

    public ValidationIssue toIssue() {
        ValidationIssue rootIssue = exportErrors();
        return rootIssue;
    }

    protected ValidationIssue exportErrors() {
        ValidationIssue issue = new ValidationIssue(getSubject());

        for (ValidationContext<?> childContext : nestedContexts) {
            ValidationIssue childIssue = childContext.exportErrors();
            if (childIssue != null) {
                issue.addSubissue(childIssue);
            }
        }

        if (hasErrors()) {
            // export errors
            for (ValidationError error : this.errors) {
                issue.addError(error);
            }
            return issue;
        } else if (issue.hasSubissues()) {
            return issue;
        }
        return null;
    }

}
