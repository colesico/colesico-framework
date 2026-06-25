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

package colesico.framework.restlet;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public interface RestletSerializer {

    <T> ByteBuffer serialize(T value, String contentType);

    <T> T deserialize(ByteBuffer data, String contentType, Type valueType);

    default <T> T deserialize(ByteBuffer data, String contentType, Class<T> valueClass) {
        return deserialize(data, (Type) valueClass);
    }

    default <T> T deserialize(InputStream is, Type valueType) {
        try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            return deserialize(reader, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default <T> T deserialize(InputStream is, Class<T> valueClass) {
        try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            return deserialize(reader, valueClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default <T> T deserialize(String text, Type valueType) {
        try (Reader reader = new StringReader(text)) {
            return deserialize(reader, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default <T> T deserialize(String text, Class<T> valueClass) {
        try (Reader reader = new StringReader(text)) {
            return deserialize(reader, valueClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

