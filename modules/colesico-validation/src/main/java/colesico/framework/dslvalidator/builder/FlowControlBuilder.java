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

package colesico.framework.dslvalidator.builder;

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.DSLValidator;
import colesico.framework.dslvalidator.Sequence;
import colesico.framework.dslvalidator.commands.*;
import colesico.framework.dslvalidator.t9n.ValidatorMessages;
import colesico.framework.translation.Translatable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

abstract public class FlowControlBuilder {

    public static final String BUILD_METHOD = "build";
    public static final String PROGRAM_METHOD = "program";
    public static final String FIELD_METHOD = "field";

    protected final ValidatorMessages vrMessages;

    public FlowControlBuilder(ValidatorMessages vrMessages) {
        this.vrMessages = vrMessages;
    }

    /**
     * Defines validation algorithm based on {@link GroupSequence }
     */
    protected final <V> DSLValidator<V> program(final Command<V>... commands) {
        GroupSequence<V> sequence = new GroupSequence<>();
        for (Command<V> cmd : commands) {
            sequence.addCommand(cmd);
        }
        return new DSLValidator<>(sequence, null);
    }

    protected final <V> DSLValidator<V> program(Sequence<V, V> commands) {
        return new DSLValidator<>(commands, null);
    }

    /**
     * Defines validation algorithm based on {@link ChainSequence }
     */
    protected final <V> DSLValidator<V> program(final String subject, final Command<V>... commands) {
        ChainSequence<V> sequence = new ChainSequence<>();
        for (Command<V> cmd : commands) {
            sequence.addCommand(cmd);
        }
        return new DSLValidator<>(sequence, subject);
    }

    /**
     * Executes commands within the local context.
     * In case of local validation errors occur, command execution is NOT interrupted.
     *
     * @param commands
     * @return
     * @see GroupSequence
     */
    protected final <V> Command<V> group(final Command<V>... commands) {
        GroupSequence<V> sequence = new GroupSequence<>();
        for (Command<V> cmd : commands) {
            sequence.addCommand(cmd);
        }
        return sequence;
    }

    /**
     * Executes commands within the local context.
     * In case of local validation errors occur, command execution is interrupted.
     *
     * @param commands
     * @return
     */
    protected final <V> Command<V> chain(final Command<V>... commands) {
        ChainSequence<V> sequence = new ChainSequence<>();
        for (Command<V> cmd : commands) {
            sequence.addCommand(cmd);
        }
        return sequence;
    }

    /**
     * Executes commands within the new nested context with specified subject.
     * In case of local validation errors occur, commands execution is interrupted.
     *
     * @param subject
     * @param commands
     * @return
     * @see SubjectSequence
     */
    protected final <V> Command<V> subject(final String subject, final Command<V>... commands) {
        SubjectSequence<V> sequence = new SubjectSequence<>(subject);
        for (Command<V> cmd : commands) {
            sequence.addCommand(cmd);
        }
        return sequence;
    }

    /**
     * Creates new nested context with the subject and the value, extracted from the value of the local context.
     * Execute commands within that nested context.
     * In case of validation errors occur in the nested context, command execution is interrupted.
     *
     * @see FieldSequence
     */
    protected final <V, N> Command<V> field(final String subject, final Function<V, N> extractor, final Command<N>... commands) {
        FieldSequence<V, N> sequence = new FieldSequence<>(subject, extractor);
        for (Command<N> cmd : commands) {
            sequence.addCommand(cmd);
        }
        return sequence;
    }

    /**
     * In case of local  validation errors occur, command execution is interrupted.
     *
     * @return
     */
    protected final <V extends List<E>, E> Command<V> item(final String subject, final int index, final Command<E>... commands) {
        ListSequence<V, E> sequence = new ListSequence<>(subject, index);
        for (Command<E> cmd : commands) {
            sequence.addCommand(cmd);
        }
        return sequence;
    }

    /**
     * Map emtry
     */
    protected final <V extends Map<K, E>, K, E> Command<V> entry(final String subject, final K key, final Command<E>... commands) {
        MapSequence<V, K, E> sequence = new MapSequence<>(subject, key);
        for (Command<E> cmd : commands) {
            sequence.addCommand(cmd);
        }
        return sequence;
    }

    /**
     * Iterates the elements of value from local context.
     * In case of  local validation errors occur, command execution is interrupted.
     *
     * @see IterableSequence
     */
    protected final <V extends Iterable<I>, I> Command<V> forEach(final Command<I>... commands) {
        IterableSequence<V, I> sequence = new IterableSequence<>();
        for (Command<I> cmd : commands) {
            sequence.addCommand(cmd);
        }
        return sequence;
    }

    /**
     * Executes sequence commands if context value is not null
     */
    protected final <V> Command<V> optional(final Command<V>... commands) {
        OptionalSequence<V> sequence = new OptionalSequence<>();
        for (Command<V> cmd : commands) {
            sequence.addCommand(cmd);
        }
        return sequence;
    }

    /**
     * Executes sequence commands if context value is not null,
     * otherwise throws validation error.
     */
    protected final <V> Command<V> mandatory(final Command<V>... commands) {
        OptionalSequence<V> sequence = new OptionalSequence<>();
        for (Command<V> cmd : commands) {
            sequence.addCommand(cmd);
        }
        return sequence;
    }

    protected final <V> Command<V> hookError(String errorCode, Translatable errorMsg, final Command<V>... commands) {
        HookErrorSequence<V> sequence = new HookErrorSequence<>(errorCode, errorMsg);
        for (Command<V> cmd : commands) {
            sequence.addCommand(cmd);
        }
        return sequence;
    }

    /**
     * Helper to create generic commands array
     */
    protected <V> Command[] commands(Command<V>... cmd) {
        return cmd;
    }
}
