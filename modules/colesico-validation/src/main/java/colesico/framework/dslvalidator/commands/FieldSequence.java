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

import colesico.framework.dslvalidator.ValidationContext;

import java.util.function.Function;

/**
 * Extract field value from current context value with extractor function
 * and applies commands to that nested value within new nested context.
 *
 * @author Vladlen Larionov
 */
public final class FieldSequence<V, N> extends AbstractSequence<V, N> {

    /**
     * Nested context subject
     */
    private final String subject;

    /**
     * Extracts nested value from value
     */
    private final Function<V, N> extractor;

    public FieldSequence(String subject, Function<V, N> extractor) {
        this.subject = subject;
        this.extractor = extractor;
    }

    @Override
    public void execute(ValidationContext<V> context) {
        N nestedValue = extractor.apply(context.getValue());
        ValidationContext<N> nestedContext = ValidationContext.ofNested(context, subject, nestedValue);
        executeChain(nestedContext);
    }
}
