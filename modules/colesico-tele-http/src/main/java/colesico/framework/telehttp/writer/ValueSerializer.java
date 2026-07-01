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

package colesico.framework.telehttp.writer;

import java.io.OutputStream;
import java.util.Map;

/**
 * Strategy interface for object serialization.
 * Implementations must be thread-safe.
 * A specific serializer corresponds to the given mime-types (e.g. text/plain, text/html ),
 * and its production is annotated with the @Named(mime-type) annotation.
 *
 * @param <T> supported value type
 */
public interface ValueSerializer<T> {

    /**
     * Serializes an object to the output stream .
     *
     * @param value        object to serialize
     * @param mediaParams  serialization params
     * @param outputStream stream to write into
     */
    void serialize(T value, Map<String, String> mediaParams, OutputStream outputStream);

}

