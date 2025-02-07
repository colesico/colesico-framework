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

import java.util.ArrayList;
import java.util.List;

/**
 * Command collection executor
 *
 * @param <V> type of value to which the sequence applies
 * @author Vladlen Larionov
 */
abstract public class Iterator<V> implements Command<V> {

    protected final List<Command<V>> commands = new ArrayList<>();

    public Iterator() {
    }

    public Iterator(Command<V>[] commands) {
        if (commands == null || commands.length == 0) {
            throw new RuntimeException(this.getClass().getSimpleName() + ": validation commands collection is null or empty");
        }
        appendAll(commands);
    }

    public void append(Command<V> command) {
        commands.add(command);
    }

    public void prepend(Command<V> command) {
        commands.addFirst(command);
    }

    public void appendAll(Command<V>[] commands) {
        for (Command<V> cmd : commands) {
            append(cmd);
        }
    }

    public void prependAll(Command<V>[] commands) {
        for (Command<V> cmd : commands) {
            prepend(cmd);
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "commands=" + commands +
                '}';
    }
}
