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

import colesico.framework.validation.ValidationError;
import colesico.framework.validation.ValidationIssue;

import java.util.*;

/**
 * Validation context
 *
 * @author Vladlen Larionov
 */
public final class ValidationContext<V> {

    // Name of value
    private final String subject;

    // Value to  be validated
    private V value;

    // Validation parameters - any values that can be used by validators.
    private Object[] params;

    // Validation errors
    private final List<ValidationError> errors = new ArrayList<>();

    // Reference to  parent context
    private final ValidationContext<?> superContext;

    // Nested contexts ref. (subject->context)
    private final Map<String, ValidationContext> nestedContexts = new LinkedHashMap<>();

    private ValidationContext(ValidationContext superContext, String subject, V value, Object... params) {
        this.subject = subject;
        this.value = value;
        this.superContext = superContext;
        this.params = params;
    }

    /**
     * Produce Root ValidationContext
     *
     * @param value
     * @param <V>
     * @return
     */
    public static <V> ValidationContext<V> ofRoot(String subject, V value, Object... params) {
        return new ValidationContext(null, subject, value, params);
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
    public static <V> ValidationContext<V> ofNested(ValidationContext<?> superContext, String subject, V value, Object... params) {
        ValidationContext<V> childContext = new ValidationContext(superContext, subject, value, params);
        superContext.nestedContexts().put(childContext.subject(), childContext);
        return childContext;
    }

    /**
     * Returns root validation context
     */
    public <U> ValidationContext<U> rootContext() {
        ValidationContext curCtx = this;
        while (curCtx.superContext() != null) {
            curCtx = curCtx.superContext();
        }
        return curCtx;
    }

    /**
     * Finds deep nested context specified by subjects path
     */
    public <U> ValidationContext<U> findNestedContext(String... path) {
        ValidationContext ctx = this;
        for (String subject : path) {
            ctx = (ValidationContext) ctx.nestedContexts().get(subject);
            if (ctx == null) {
                return null;
            }
        }
        return ctx;
    }

    public String subject() {
        return subject;
    }

    /**
     * Returns value from this context
     */
    public V value() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public Object[] params() {
        return params;
    }

    public <T> T getParam(int index) {
        if (params != null && index < params.length) {
            return (T) params[index];
        }
        return null;
    }

    public <T> T rootParam(int index) {
        return rootContext().getParam(index);
    }

    public Object[] rootParams() {
        return rootContext().params();
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public List<ValidationError> errors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public boolean hasNestedErrors() {
        return hasNestedErrors(this);
    }

    protected boolean hasNestedErrors(ValidationContext<V> context) {
        Collection<ValidationContext> nestedContexts = context.nestedContexts().values();
        for (ValidationContext nestedCtx : nestedContexts) {
            if (nestedCtx.hasErrors()) {
                return true;
            } else if (hasNestedErrors(nestedCtx)) {
                return true;
            }
        }
        return false;
    }

    public void addError(String code, String message) {
        ValidationError error = new ValidationError(code, message);
        errors.add(error);
    }


    public Map<String, ValidationContext> nestedContexts() {
        return nestedContexts;
    }

    public ValidationContext<?> superContext() {
        return superContext;
    }

    public ValidationIssue toIssue() {
        ValidationIssue rootIssue = exportErrors();
        return rootIssue;
    }

    protected ValidationIssue exportErrors() {
        ValidationIssue issue = new ValidationIssue(subject());

        for (ValidationContext<?> childContext : nestedContexts.values()) {
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

    @Override
    public String toString() {
        return "ValidationContext{" +
                "subject='" + subject + '\'' +
                ", value=" + value +
                '}';
    }
}
