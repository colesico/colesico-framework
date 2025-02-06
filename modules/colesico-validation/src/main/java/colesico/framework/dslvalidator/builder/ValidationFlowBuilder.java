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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

abstract public class ValidationFlowBuilder {

    public static final String FIELD_METHOD = "field";
    public static final String OPTIONAL_GROUP_METHOD = "optionalGroup";
    public static final String MANDATORY_GROUP_METHOD = "mandatoryGroup";

    protected final ValidatorMessages vrMessages;

    public ValidationFlowBuilder(ValidatorMessages vrMessages) {
        this.vrMessages = vrMessages;
    }

    /**
     * Basic validator instance
     */
    protected final <V> DSLValidator<V> basicValidator(final String subject, Command<V> program) {
        return new DSLValidator<>(subject, program);
    }

    protected final <V> DSLValidator<V> basicValidator(Command<V> program) {
        return new DSLValidator<>(program);
    }

    /**
     * Validator instance with program based on {@link MandatoryGroup }
     */
    protected final <V> DSLValidator<V> validator(final String subject, final Command<V>... commands) {
        var sequence = new MandatoryGroup<>(vrMessages, commands);
        return new DSLValidator<>(subject, sequence);
    }

    protected final <V> DSLValidator<V> validator(final Command<V>... commands) {
        var sequence = new MandatoryGroup<>(vrMessages, commands);
        return new DSLValidator<>(sequence);
    }

    /**
     * Executes group commands  within the local context if context value is not null,
     * otherwise throws validation error.
     * In case of local validation errors occur, command execution is NOT interrupted.
     *
     * @see GroupSequence
     */
    protected final <V> Command<V> group(final Command<V>... commands) {
        return new GroupSequence<>(commands);
    }

    /**
     * Executes group commands if context value is not null.
     * In case of local validation errors occur, command execution is NOT interrupted.
     */
    protected final <V> Command<V> optionalGroup(final Command<V>... commands) {
        return new ConditionalGroup<>(c -> c.getValue() != null, commands);
    }

    /**
     * @see ConditionalGroup
     */
    protected final <V> Command<V> conditionalGroup(Predicate<ValidationContext> condition, final Command<V>... commands) {
        return new ConditionalGroup<>(condition, commands);
    }

    /**
     * @see MandatoryGroup
     */
    protected final <V> Command<V> mandatoryGroup(final Command<V>... commands) {
        return new MandatoryGroup<>(vrMessages, commands);
    }

    /**
     * Executes commands within the local context.
     * In case of local validation errors occur, command execution is interrupted.
     *
     * @see ChainSequence
     */
    protected final <V> Command<V> chain(final Command<V>... commands) {
        return new ChainSequence<>(commands);
    }

    /**
     * @see ConditionalChain
     */
    protected final <V> Command<V> optionalChain(final Command<V>... commands) {
        return new ConditionalChain<>(c -> c.getValue() != null, commands);
    }

    protected final <V> Command<V> conditionalChain(Predicate<ValidationContext> condition, final Command<V>... commands) {
        return new ConditionalChain<>(condition, commands);
    }

    /**
     * @see MandatoryChain
     */
    protected final <V> Command<V> mandatoryChain(final Command<V>... commands) {
        return new MandatoryChain<>(vrMessages, commands);
    }

    /**
     * Executes commands within the new nested context with specified subject.
     * In case of local validation errors occur, commands execution is interrupted.
     *
     * @see SubjectChain
     */
    protected final <V> Command<V> subject(final String subject, final Command<V>... commands) {
        return new SubjectChain<>(subject, commands);
    }

    /**
     * Creates new nested context with the subject and the value, extracted from the value of the local context.
     * Execute commands within that nested context.
     * In case of validation errors occur in the nested context, command execution is interrupted.
     *
     * @see FieldChain
     */
    protected final <V, N> Command<V> field(final String subject, final Function<V, N> extractor, final Command<N>... commands) {
        return new FieldChain<>(subject, extractor, commands);
    }

    protected final <V, N> Command<V> field(final FieldReference<V, N> filedRef, final Command<N>... commands) {
        return new FieldChain<>(filedRef.subject(), filedRef.extractor(), commands);
    }

    /**
     * @see NestedChain
     */
    protected final <V, N> Command<V> nested(final String subject, final Function<Optional<V>, Optional<N>> extractor, final Command<N>... commands) {
        return new NestedChain<>(subject, extractor, commands);
    }

    /**
     * Array/List item
     *
     * @see ItemChain
     */
    protected final <V extends List<E>, E> Command<V> item(final String subject, final int index, final Command<E>... commands) {
        return new ItemChain<>(subject, index, commands);
    }

    /**
     * Map entry
     *
     * @see EntryChain
     */
    protected final <V extends Map<K, E>, K, E> Command<V> entry(final String subject, final K key, final Command<E>... commands) {
        return new EntryChain<>(subject, key, commands);
    }

    /**
     * Iterates the elements of value from local context.
     * In case of  local validation errors occur, command execution is interrupted.
     *
     * @see IterableChain
     */
    protected final <V extends Iterable<I>, I> Command<V> forEach(final Command<I>... commands) {
        return new IterableChain<>(commands);
    }

    /**
     * @see HookErrorChain
     */
    protected final <V> Command<V> hookError(String errorCode, Translatable errorMsg, final Command<V>... commands) {
        return new HookErrorChain<>(errorCode, errorMsg, commands);
    }

    /**
     * Helper to  create generic commands array
     */
    protected <V> Command[] commands(Command<V>... cmd) {
        return cmd;
    }
}
