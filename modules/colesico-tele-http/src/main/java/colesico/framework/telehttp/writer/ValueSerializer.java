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

import colesico.framework.telehttp.MediaType;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Strategy interface for object serialization and deserialization.
 * Implementations must be thread-safe.
 */
public interface ValueSerializer {

    /**
     * Serializes an object to the output stream using the specified media type.
     *
     * @param value        the object to serialize
     * @param mediaType    the target data format (e.g., JSON, XML)
     * @param outputStream the destination stream to write into
     */
    void serialize(Object value, MediaType mediaType, OutputStream outputStream);

    default ByteBuffer serialize(Object value, MediaType mediaType) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        serialize(value, mediaType, baos);
        return ByteBuffer.wrap(baos.toByteArray());
    }
}

