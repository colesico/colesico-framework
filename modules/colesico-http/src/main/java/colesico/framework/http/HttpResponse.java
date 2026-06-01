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

import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;

/**
 * @author Vladlen Larionov
 */
public interface HttpResponse {

    HttpResponse setStatus(Integer code);

    HttpResponse setContentType(String contentType);

    HttpResponse setCookie(HttpCookie cookie);

    HttpResponse setHeader(String name, String vale);

    /**
     * Send text response
     */
    void sendText(String text);

    /**
     * Send binary response
     */
    void sendData(ByteBuffer buffer);

    /**
     * Send redirect  (Http header Location)
     */
    void sendRedirect(String location);

    OutputStream outputStream();

    /**
     * Returns true if the server has sent any data to a client
     */
    boolean isCommitted();

    /**
     * Dump response data to characters output for further logging
     */
    void dump(Writer out);
}
