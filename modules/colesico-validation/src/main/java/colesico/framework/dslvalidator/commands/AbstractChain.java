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

package colesico.framework.dslvalidator.commands;

import colesico.framework.dslvalidator.Chain;
import colesico.framework.dslvalidator.Command;
import colesico.framework.dslvalidator.ValidationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic class for command chain
 *
 * @author Vladlen Larionov
 */
abstract public class AbstractChain<V> implements Chain<V> {

    protected final ArrayList<Command<?>> commands = new ArrayList<>();

    @Override
    public List<Command<?>> getCommands() {
        return commands;
    }

    /**
     * Executes chain commands until current context has no errors.
     * @param context
     */
    protected void executeCommands(ValidationContext<?> context) {
        for (Command command : commands) {
            if (context.hasErrors()) {
                return;
            }
            command.execute(context);
        }
    }

}
