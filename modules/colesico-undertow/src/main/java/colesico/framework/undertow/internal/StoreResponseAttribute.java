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

import io.undertow.UndertowLogger;
import io.undertow.attribute.ExchangeAttribute;
import io.undertow.attribute.ReadOnlyAttributeException;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class StoreResponseAttribute implements ExchangeAttribute {

    public static final ExchangeAttribute INSTANCE = new StoreResponseAttribute();

    private StoreResponseAttribute() {
    }

    public String readAttribute(HttpServerExchange exchange) {
        byte[] data = (byte[]) exchange.getAttachment(StoreResponseConduit.RESPONSE);
        if (data == null) {
            return null;
        } else {
            String charset = this.extractCharset(exchange.getResponseHeaders());
            if (charset == null) {
                return null;
            } else {
                try {
                    return new String(data, charset);
                } catch (UnsupportedEncodingException var5) {
                    UndertowLogger.ROOT_LOGGER.debugf(var5, "Could not decode response body using charset %s", charset);
                    return null;
                }
            }
        }
    }

    private String extractCharset(HeaderMap headers) {
        String contentType = headers.getFirst(Headers.CONTENT_TYPE);
        if (contentType != null) {
            String value = Headers.extractQuotedValueFromHeader(contentType, "charset");
            if (value != null) {
                return value;
            } else {
                return contentType.startsWith("text/") ? StandardCharsets.ISO_8859_1.displayName() : null;
            }
        } else {
            return null;
        }
    }

    public void writeAttribute(HttpServerExchange exchange, String newValue) throws ReadOnlyAttributeException {
        throw new ReadOnlyAttributeException("StoreResponseAttribute", newValue);
    }

}
