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

package colesico.framework.teleapi;

/**
 * Remote data reader.
 * For cases that the data port is built on a pluggable/modular architecture in which a separate data
 * reader or writer is used for given data type.
 * <p>
 * Reader is used to read param values from remote client.
 * Reader must be a stateless  (may be a singleton but stateless)
 *
 * @param <V> the value  type to be read
 * @param <C> the reading context
 */
public interface TeleReader<V, C extends TRContext> {
    V read(C context);
}
