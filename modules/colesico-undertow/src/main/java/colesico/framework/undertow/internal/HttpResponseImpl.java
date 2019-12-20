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
import colesico.framework.http.HttpResponse;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import io.undertow.server.handlers.CookieImpl;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import io.undertow.util.StatusCodes;

import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * @author Vladlen Larionov
 */
public class HttpResponseImpl implements HttpResponse {

    protected final HttpServerExchange exchange;

    public HttpResponseImpl(HttpServerExchange exchange) {
        this.exchange = exchange;
    }


    public HttpServerExchange getExchange() {
        return exchange;
    }

    @Override
    public OutputStream getOutputStream() {
        exchange.startBlocking();
        return exchange.getOutputStream();
    }

    @Override
    public void setStatusCode(Integer code) {
        exchange.setStatusCode(code);
    }

    @Override
    public void setContenType(String contentType) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, contentType);
    }

    protected void sendMetadata(String contentType, Integer statusCode) {
        if (contentType == null) {
            contentType = "text/html; charset=utf-8";
        }
        if (statusCode == null) {
            statusCode = 200;
        }
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, contentType);
        exchange.setStatusCode(statusCode);
    }

    @Override
    public void sendText(String text, String contentType, Integer statusCode) {
        sendMetadata(contentType, statusCode);
        exchange.getResponseSender().send(text);
    }

    @Override
    public void sendData(ByteBuffer byteBuffer, String contentType, Integer statusCode) {
        sendMetadata(contentType, statusCode);
        exchange.getResponseSender().send(byteBuffer);
    }


    @Override
    public void setCookie(HttpCookie cookie) {
        //responded = true;
        Cookie c = new CookieImpl(cookie.getName(), cookie.getValue());
        c.setDomain(cookie.getDomain());
        c.setPath(cookie.getPath());
        c.setExpires(cookie.getExpires());
        if (cookie.getSecure() != null && cookie.getSecure()) {
            c.setSecure(true);
        }
        if (cookie.getHttpOnly() != null && cookie.getHttpOnly()) {
            c.setHttpOnly(true);
        }
        if (cookie.getSameSite() != null) {
            switch (cookie.getSameSite()) {
                case STRICT:
                    c.setSameSiteMode("strict");
                    break;
                case LAX:
                    c.setSameSiteMode("lax");
                    break;
            }
        }
        exchange.setResponseCookie(c);
    }

    @Override
    public void setHeader(String name, String vale) {
        exchange.getResponseHeaders().put(new HttpString(name), vale);
    }

    @Override
    public void sendRedirect(String location, Integer code) {
        if (code == null) {
            code = StatusCodes.FOUND;
        }
        exchange.setStatusCode(code);
        exchange.getResponseHeaders().put(Headers.LOCATION, location);
        exchange.endExchange();
    }

    @Override
    public boolean isResponded() {
        return exchange.isResponseStarted();
    }

}
