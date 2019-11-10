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

import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.Sequence;
import colesico.framework.dslvalidator.ValidationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Sequence basis
 * @param <V>
 * @param <C>
 */
abstract public class AbstractSequence<V, C> implements Sequence<V, C> {

    protected final ArrayList<Command<C>> commands = new ArrayList<>();

    @Override
    public List<Command<C>> getCommands() {
        return commands;
    }

    /**
     * Execute sequence commands until error occurred
     *
     * @param context
     */
    protected void executeChain(ValidationContext<C> context) {
        for (Command<C> command : commands) {
            if (context.hasErrors()) {
                return;
            }
            command.execute(context);
        }
    }

    /**
     * Executes all sequence commands
     *
     * @param context
     */
    protected void executeGroup(ValidationContext<C> context) {
        for (Command<C> command : commands) {
            command.execute(context);
        }
    }

}
