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

package colesico.framework.dslvalidator.builder;

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.DSLValidator;
import colesico.framework.dslvalidator.ValidationContext;
import colesico.framework.dslvalidator.command.*;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;
import colesico.framework.translation.Translatable;

import java.util.function.Function;
import java.util.function.Predicate;

abstract public class ValidationFlowBuilder {

    public static final String MAP_METHOD = "map";

    protected final ValidatorMessages msg;

    public ValidationFlowBuilder(ValidatorMessages msg) {
        this.msg = msg;
    }

    /**
     * Basic validator instance
     */
    protected final <V> DSLValidator<V> basicValidator(final String subject, Command<V> command) {
        return new DSLValidator<>(subject, command);
    }

    protected final <V> DSLValidator<V> basicValidator(Command<V> command) {
        return new DSLValidator<>(command);
    }

    /**
     * Validator instance with validation based on {@link MandatoryExecutor }
     */
    protected final <V> DSLValidator<V> validator(final String subject, final Command<V>... commands) {
        SeriesIterator<V> seriesIterator = new SeriesIterator<>(commands);
        MandatoryExecutor<V> mandatoryExecutor = new MandatoryExecutor<>(msg, seriesIterator);
        return new DSLValidator<>(subject, mandatoryExecutor);
    }

    protected final <V> DSLValidator<V> validator(final Command<V>... commands) {
        SeriesIterator<V> seriesIterator = new SeriesIterator<>(commands);
        MandatoryExecutor<V> mandatoryExecutor = new MandatoryExecutor<>(msg, seriesIterator);
        return new DSLValidator<>(mandatoryExecutor);
    }

    /**
     * Executes commands  within the local context if context value is not null,
     * otherwise throws validation error.
     * In case of local validation errors occur, command execution is NOT interrupted.
     *
     * @see SeriesIterator
     */
    protected final <V> SeriesIterator<V> series(final Command<V>... commands) {
        return new SeriesIterator<>(commands);
    }

    /**
     * Executes commands within the local context.
     * In case of local validation errors occur, command execution is interrupted.
     *
     * @see ChainIterator
     */
    protected final <V> ChainIterator<V> chain(final Command<V>... commands) {
        return new ChainIterator<>(commands);
    }

    /**
     * @see OptionalExecutor
     * @see ChainIterator
     */
    protected final <V> OptionalExecutor<V> optional(final Command<V>... commands) {
        ChainIterator<V> chainIterator = new ChainIterator<>(commands);
        return new OptionalExecutor<>(chainIterator);
    }

    /**
     * @see MandatoryExecutor
     * @see ChainIterator
     */
    protected final <V> MandatoryExecutor<V> mandatory(final Command<V>... commands) {
        ChainIterator<V> chainIterator = new ChainIterator<>(commands);
        return new MandatoryExecutor<>(msg, chainIterator);
    }

    /**
     * @see SeriesIterator
     * @see ConditionalExecutor
     */
    protected final <V> ConditionalExecutor<V> conditional(Predicate<ValidationContext<V>> condition, final Command<V>... commands) {
        SeriesIterator<V> seriesIterator = new SeriesIterator<>(commands);
        return new ConditionalExecutor<>(condition, seriesIterator);
    }

    /**
     * Executes commands within the new nested context with specified subject.
     * In case of local validation errors occur, commands execution is interrupted.
     *
     * @see SubjectExecutor
     * @see ChainIterator
     */
    protected final <V> SubjectExecutor<V> subject(final String subject, final Command<V>... commands) {
        ChainIterator<V> chainIterator = new ChainIterator<>(commands);
        return new SubjectExecutor<>(subject, chainIterator);
    }

    /**
     * Creates new nested context with the subject and the value, extracted from the value of the local context.
     */
    protected final <V, N> ValueMapper<V, N> map(final String subject, final Function<V, N> mapper, final Command<N> command) {
        return new ValueMapper<>(subject, mapper, command);
    }

    /**
     * Creates new nested context with the subject and the value, extracted from the value of the local context.
     * Execute commands within that nested context.
     * In case of validation errors occur in the nested context, command execution is interrupted.
     *
     * @see ValueMapper
     * @see ChainIterator
     */
    protected final <V, N> ValueMapper<V, N> map(final String subject, final Function<V, N> mapper, final Command<N>... commands) {
        ChainIterator<N> chainIterator = new ChainIterator<>(commands);
        return new ValueMapper<>(subject, mapper, chainIterator);
    }

    protected final <V, N> ValueMapper<V, N> map(final FieldReference<V, N> filedRef, final Command<N>... commands) {
        ChainIterator<N> chainIterator = new ChainIterator<>(commands);
        return new ValueMapper<>(filedRef.subject(), filedRef.mapper(), chainIterator);
    }

    /**
     * Iterates the elements of value from local context.
     * In case of  local validation errors occur, command execution is interrupted.
     *
     * @see IterableMapper
     */
    protected final <V extends Iterable<I>, I> Command<V> forEach(String subject, final Command<I>... commands) {
        ChainIterator<I> chainIterator = new ChainIterator<>(commands);
        return new IterableMapper<>(subject, chainIterator);
    }

    /**
     * @see HookErrorExecuror
     */
    protected final <V> HookErrorExecuror<V> hookError(String errorCode, Translatable errorMsg, final Command<V>... commands) {
        ChainIterator<V> chainIterator = new ChainIterator<>(commands);
        return new HookErrorExecuror<>(errorCode, errorMsg, null, chainIterator);
    }

    /**
     * Helper to  create generic commands array
     */
    @SafeVarargs
    protected final <V> Command<V>[] commands(Command<V>... cmd) {
        return cmd;
    }
}
