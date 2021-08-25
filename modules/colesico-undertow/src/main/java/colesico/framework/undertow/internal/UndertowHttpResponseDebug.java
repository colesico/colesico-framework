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
package colesico.framework.undertow.internal;

import colesico.framework.http.HttpCookie;
import io.undertow.server.HttpServerExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * @author Vladlen Larionov
 */
public class UndertowHttpResponseDebug extends UndertowHttpResponse {

    protected static final Logger log = LoggerFactory.getLogger(UndertowHttpResponseDebug.class);
    protected final String responseId;

    public UndertowHttpResponseDebug(HttpServerExchange exchange) {
        super(exchange);
        this.responseId = UUID.randomUUID().toString();
    }

    public HttpServerExchange getExchange() {
        return exchange;
    }

    @Override
    public OutputStream getOutputStream() {
        log.debug("getOutputStream(); responded=" + isResponded() + "; responseId=" + responseId);
        return super.getOutputStream();
    }

    @Override
    public void setStatusCode(Integer code) {
        log.debug("setStatusCode(" + code + "); responded=" + isResponded() + "; responseId=" + responseId);
        super.setStatusCode(code);
    }

    @Override
    public void setContenType(String contentType) {
        log.debug("setContenType(" + contentType + "); responded=" + isResponded() + "; responseId=" + responseId);
        super.setContenType(contentType);
    }

    @Override
    public void sendText(String text, String contentType, Integer statusCode) {
        log.debug("sendText(" + text + "," + contentType + "," + statusCode + "); responded=" + isResponded() + "; responseId=" + responseId);
        super.sendText(text, contentType, statusCode);
    }

    @Override
    public void sendData(ByteBuffer byteBuffer, String contentType, Integer statusCode) {
        log.debug("sendData(" + byteBuffer + "," + contentType + "," + statusCode + "); responded=" + isResponded() + "; responseId=" + responseId);
        super.sendData(byteBuffer, contentType, statusCode);
    }

    @Override
    public void setCookie(HttpCookie cookie) {
        log.debug("setCookie(" + cookie + "); responded=" + isResponded() + "; responseId=" + responseId);
        super.setCookie(cookie);
    }

    @Override
    public void setHeader(String name, String vale) {
        log.debug("setHeader(" + name + "," + vale + "); responded=" + isResponded() + "; responseId=" + responseId);
        super.setHeader(name, vale);
    }

    @Override
    public void sendRedirect(String location, Integer code) {
        log.debug("sendRedirect(" + location + "," + code + "); responded=" + isResponded() + "; responseId=" + responseId);
        super.sendRedirect(location, code);
    }
}
