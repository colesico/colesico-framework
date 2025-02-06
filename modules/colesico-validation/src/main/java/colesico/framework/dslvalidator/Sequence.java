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

import java.util.List;

/**
 * Sequence of commands
 *
 * @param <V> type of value to which the sequence applies
 * @param <N> type of nested value  (extracted from value) to which the sequence commands applies
 * @author Vladlen Larionov
 */
public interface Sequence<V, N> extends Command<V> {

    List<Command<N>> getCommands();

    default Sequence<V, N> append(Command<N> command) {
        getCommands().add(command);
        return this;
    }

    default Sequence<V, N> prepend(Command<N> command) {
        getCommands().addFirst(command);
        return this;
    }

    default Sequence<V, N> appendAll(Command<N>[] commands) {
        for (Command<N> cmd : commands) {
            append(cmd);
        }
        return this;
    }

    default Sequence<V, N> prependAll(Command<N>[] commands) {
        for (Command<N> cmd : commands) {
            prepend(cmd);
        }
        return this;
    }
}
