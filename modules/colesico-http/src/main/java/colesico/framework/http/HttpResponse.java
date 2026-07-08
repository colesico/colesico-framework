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
package colesico.framework.http;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author Vladlen Larionov
 */
public interface HttpResponse {

    HttpResponse setStatus(Integer code);

    HttpResponse addHeader(String name, String vale);

    HttpResponse addCookie(HttpCookie cookie);

    OutputStream outputStream();

    /**
     * Close response
     */
    void close();

    /**
     * Returns true if the server has sent any data to a client
     */
    boolean isCommitted();

    /**
     * Dump response data to characters output for further logging
     */
    void dump(Writer out);

    default HttpResponse setContentType(String contentType) {
        addHeader("Content-Type", contentType);
        return this;
    }

    /**
     * Response with bytes
     */
    default void send(byte[] bytes) {
        try (var os = outputStream()) {
            os.write(bytes);
            os.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Response with text in utf-8
     */
    default void send(String text) {
        send(text.getBytes(StandardCharsets.UTF_8));
    }

    default void send(String text, Charset charset) {
        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }
        send(text.getBytes(charset));
    }
}
