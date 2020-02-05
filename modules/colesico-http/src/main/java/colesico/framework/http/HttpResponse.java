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
package colesico.framework.http;

import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;

/**
 * @author Vladlen Larionov
 */
public interface HttpResponse {

    void setStatusCode(Integer code);

    void setContenType(String contentType);

    void setCookie(HttpCookie cookie);

    void setHeader(String name, String vale);

    /**
     * Send text response
     */
    void sendText(String text, String contentType, Integer statusCode);

    /**
     * Send binary response
     */
    void sendData(ByteBuffer byteBuffer, String contentType, Integer statusCode);

    /**
     * Send redirect  (Http header Location)
     */
    void sendRedirect(String location, Integer statusCode);

    default void sendRedirect(String location) {
        sendRedirect(location, 302);
    }

    OutputStream getOutputStream();

    /**
     * Returns true if the server has sent any data to a client
     *
     * @return
     */
    boolean isResponded();

    /**
     * Dump response data to characters output for further logging
     */
    void dump(Writer out);
}
