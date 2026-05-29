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
package colesico.framework.undertow.internal;

import colesico.framework.http.HttpCookie;
import colesico.framework.http.HttpResponse;
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

    public HttpServerExchange exchange() {
        return exchange;
    }

    @Override
    public OutputStream outputStream() {
        log.debug("getOutputStream(); responded={}; responseId={}", isResponded(), responseId);
        return super.outputStream();
    }

    @Override
    public HttpResponse setStatusCode(Integer code) {
        log.debug("setStatusCode({}); responded={}; responseId={}", code, isResponded(), responseId);
        super.setStatusCode(code);
        return this;
    }

    @Override
    public HttpResponse setContentType(String contentType) {
        log.debug("setContenType({}); responded={}; responseId={}", contentType, isResponded(), responseId);
        super.setContentType(contentType);
        return this;
    }

    @Override
    public HttpResponse setCookie(HttpCookie cookie) {
        log.debug("setCookie({}); responded={}; responseId={}", cookie, isResponded(), responseId);
        super.setCookie(cookie);
        return this;
    }

    @Override
    public HttpResponse setHeader(String name, String vale) {
        log.debug("setHeader({},{}); responded={}; responseId={}", name, vale, isResponded(), responseId);
        super.setHeader(name, vale);
        return this;
    }

    @Override
    public void sendText(String text) {
        log.debug("sendText({}); responded={}; responseId={}", text, isResponded(), responseId);
        super.sendText(text);
    }

    @Override
    public void sendData(ByteBuffer byteBuffer) {
        log.debug("sendData({}); responded={}; responseId={}", byteBuffer, isResponded(), responseId);
        super.sendData(byteBuffer);
    }

    @Override
    public void sendRedirect(String location) {
        log.debug("sendRedirect({}); responded={}; responseId={}", location, isResponded(), responseId);
        super.sendRedirect(location);
    }
}
