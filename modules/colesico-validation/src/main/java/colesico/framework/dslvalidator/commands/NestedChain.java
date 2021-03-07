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

import java.util.Optional;
import java.util.function.Function;

/**
 * Analogue of {@link FieldChain} except current value passed to extractor as {@link Optional}
 * This allows null safe access to nested fields : o->o.map(t->t.getA()).map(a->a.getB()).map(b->b.getC())...
 *
 * @author Vladlen Larionov
 */
public final class NestedChain<V, N> extends AbstractSequence<V, N> {

    /**
     * Nested context subject
     */
    private final String subject;

    /**
     * Extracts nested value from value
     */
    private final Function<Optional<V>, Optional<N>> extractor;

    public NestedChain(String subject, Function<Optional<V>, Optional<N>> extractor) {
        this.subject = subject;
        this.extractor = extractor;
    }

    @Override
    public void execute(ValidationContext<V> context) {
        Optional<V> value = Optional.ofNullable(context.getValue());
        Optional<N> nestedValue = extractor.apply(value);
        ValidationContext<N> nestedContext = ValidationContext.ofNested(context, subject, nestedValue.orElse(null));
        executeChain(nestedContext);
    }
}

