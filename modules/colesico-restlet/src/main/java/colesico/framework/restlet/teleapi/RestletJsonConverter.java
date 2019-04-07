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
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package colesico.framework.restlet.teleapi;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;

public interface RestletJsonConverter {
    <T> String toJson(T obj);

    <T> T fromJson(Reader reader, Type valueType);

    default <T> T fromJson(Reader reader, Class<T> valueClass) {
        return fromJson(reader, (Type) valueClass);
    }

    default <T> T fromJson(InputStream is, Type valueType) {
        try (Reader reader = new InputStreamReader(is, "UTF-8")) {
            return fromJson(reader, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default <T> T fromJson(InputStream is, Class<T> valueClass) {
        try (Reader reader = new InputStreamReader(is, "UTF-8")) {
            return fromJson(reader, valueClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default <T> T fromJson(String json, Type valueType) {
        try (Reader reader = new StringReader(json)) {
            return fromJson(reader, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default <T> T fromJson(String json, Class<T> valueClass) {
        try (Reader reader = new StringReader(json)) {
            return fromJson(reader, valueClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

