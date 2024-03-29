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
import io.undertow.util.HeaderValues;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import org.apache.commons.lang3.StringUtils;

import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 * @author Vladlen Larionov
 */
public class UndertowHttpResponse implements HttpResponse {

    protected final HttpServerExchange exchange;

    public UndertowHttpResponse(HttpServerExchange exchange) {
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
        if (StringUtils.isEmpty(contentType)) {
            throw new RuntimeException("ContentType is empty");
        }
        if (statusCode == null) {
            throw new RuntimeException("StatusCode is null");
        }
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, contentType);
        exchange.setStatusCode(statusCode);
    }

    @Override
    public void sendText(String text, String contentType, Integer statusCode) {
        sendMetadata(contentType, statusCode);
        exchange.getResponseSender().send(text);
        exchange.endExchange();
    }

    @Override
    public void sendData(ByteBuffer byteBuffer, String contentType, Integer statusCode) {
        sendMetadata(contentType, statusCode);
        exchange.getResponseSender().send(byteBuffer);
        exchange.endExchange();
    }


    @Override
    public void setCookie(HttpCookie cookie) {
        exchange.setResponseCookie(((UndertowCookie) cookie).getUndertowCookie());
    }

    @Override
    public void setHeader(String name, String vale) {
        exchange.getResponseHeaders().put(new HttpString(name), vale);
    }

    @Override
    public void sendRedirect(String location, Integer code) {
        if (code == null) {
            throw new RuntimeException("StatusCode is null");
        }
        exchange.setStatusCode(code);
        exchange.getResponseHeaders().put(Headers.LOCATION, location);
        exchange.endExchange();
    }

    @Override
    public boolean isResponded() {
        return exchange.isResponseStarted();
    }

    /**
     * To dump response body io.undertow.server.handlers.StoredResponseHandler should be added to server.
     *
     * @param out
     */
    @Override
    public void dump(Writer out) {
        try {
            out.append("status: " + exchange.getStatusCode() + "\n");
            for (HeaderValues header : exchange.getResponseHeaders()) {
                for (String value : header) {
                    out.append("header: " + header.getHeaderName() + "=" + value + "\n");
                }
            }
            Iterable<Cookie> cookies = exchange.responseCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    out.append("cookie: " + cookie.getName() + "=" + cookie.getValue() + "; domain=" + cookie.getDomain() + "; path=" + cookie.getPath() + "\n");
                }
            }
            String dumperResponse = StoreResponseAttribute.INSTANCE.readAttribute(exchange);
            if (dumperResponse != null) {
                out.append("body: \n");
                out.append(dumperResponse);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
