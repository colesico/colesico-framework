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

import java.lang.reflect.Type;

/**
 * Tele-reader is a command used by {@link DataPort} to retrieve a value
 * of a specified type considering provided options.
 * Reader must be a stateless (allowed to be a singleton)
 */
@FunctionalInterface
public interface TeleReader<V, O extends ReadOptions> {

    V read(Type baseType, O options);

    default V read(Class<V> baseType, O options) {
        return read((Type) baseType, options);
    }

}
