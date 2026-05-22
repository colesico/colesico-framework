/*
 * Copyright © 2014-2025 Vladlen V. Larionov and others as noted.
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

package colesico.framework.teleapi.dataport;

/**
 * Writer is used by {@link DataPort} to write values to channel.
 * Writer must be a stateless  (maybe a singleton but stateless)
 *
 * @param <C> channel raw api
 */
@FunctionalInterface
public interface TeleWriter<V, W extends WriteOptions<?>, C> {
    void write(V value, Class<V> valueType, W options, C channel);
}
