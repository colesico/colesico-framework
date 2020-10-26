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

    public static final String FIELD_METHOD = "field";
    public static final String GROUP_METHOD = "group";
    public static final String OPTIONAL_METHOD = "optional";
    public static final String MANDATORY_METHOD = "mandatory";

    protected final ValidatorMessages vrMessages;

    public FlowControlBuilder(ValidatorMessages vrMessages) {
        this.vrMessages = vrMessages;
    }

    protected final <V> DSLValidator<V> program(Sequence<V, V> commands) {
        return new DSLValidator<>(commands, null);
    }

    /**
     * Defines validation algorithm based on {@link GroupSequence }
     */
    protected final <V> DSLValidator<V> program(final Command<V>... commands) {
        MandatoryGroup<V> sequence = new MandatoryGroup<>(vrMessages);
        for (Command<V> cmd : commands) {
            sequence.addCommand(cmd);
        }
        return new DSLValidator<>(sequence, null);
    }

    protected final <V> DSLValidator<V> program(final String subject, Sequence<V, V> commands) {
        return new DSLValidator<>(commands, subject);
    }

    /**
     * Defines validation algorithm based on {@link ChainSequence }
     */
    protected final <V> DSLValidator<V> program(final String subject, final Command<V>... commands) {
        MandatoryChain<V> sequence = new MandatoryChain<>(vrMessages);
        for (Command<V> cmd : commands) {
            sequence.addCommand(cmd);
        }
        return new DSLValidator<>(sequence, subject);
    }

    /**
     * Executes group commands  within the local context if context value is not null,
     * otherwise throws validation error.
     * In case of local validation errors occur, command execution is NOT interrupted.
     *
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
     * Executes group commands if context value is not null.
     * In case of local validation errors occur, command execution is NOT interrupted.
     */
    protected final <V> Command<V> optionalGroup(final Command<V>... commands) {
        OptionalGroup<V> sequence = new OptionalGroup<>();
        for (Command<V> cmd : commands) {
            sequence.addCommand(cmd);
        }
        return sequence;
    }

    protected final <V> Command<V> mandatoryGroup(final Command<V>... commands) {
        MandatoryGroup<V> sequence = new MandatoryGroup<>(vrMessages);
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
     * @see OptionalChain
     */
    protected final <V> Command<V> optionalChain(final Command<V>... commands) {
        OptionalChain<V> sequence = new OptionalChain<>();
        for (Command<V> cmd : commands) {
            sequence.addCommand(cmd);
        }
        return sequence;
    }

    /**
     * @see MandatoryChain
     */
    protected final <V> Command<V> mandatoryChain(final Command<V>... commands) {
        MandatoryChain<V> sequence = new MandatoryChain<>(vrMessages);
        for (Command<V> cmd : commands) {
            sequence.addCommand(cmd);
        }
        return sequence;
    }


    /**
     * Executes commands within the new nested context with specified subject.
     * In case of local validation errors occur, commands execution is interrupted.
     *
     * @see SubjectChain
     */
    protected final <V> Command<V> subject(final String subject, final Command<V>... commands) {
        SubjectChain<V> sequence = new SubjectChain<>(subject);
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
     * @see FieldChain
     */
    protected final <V, N> Command<V> field(final String subject, final Function<V, N> extractor, final Command<N>... commands) {
        FieldChain<V, N> sequence = new FieldChain<>(subject, extractor);
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
        ListChain<V, E> sequence = new ListChain<>(subject, index);
        for (Command<E> cmd : commands) {
            sequence.addCommand(cmd);
        }
        return sequence;
    }

    /**
     * Map emtry
     */
    protected final <V extends Map<K, E>, K, E> Command<V> entry(final String subject, final K key, final Command<E>... commands) {
        MapChain<V, K, E> sequence = new MapChain<>(subject, key);
        for (Command<E> cmd : commands) {
            sequence.addCommand(cmd);
        }
        return sequence;
    }

    /**
     * Iterates the elements of value from local context.
     * In case of  local validation errors occur, command execution is interrupted.
     *
     * @see IterableChain
     */
    protected final <V extends Iterable<I>, I> Command<V> forEach(final Command<I>... commands) {
        IterableChain<V, I> sequence = new IterableChain<>();
        for (Command<I> cmd : commands) {
            sequence.addCommand(cmd);
        }
        return sequence;
    }

    protected final <V> Command<V> hookError(String errorCode, Translatable errorMsg, final Command<V>... commands) {
        HookErrorChain<V> sequence = new HookErrorChain<>(errorCode, errorMsg);
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
