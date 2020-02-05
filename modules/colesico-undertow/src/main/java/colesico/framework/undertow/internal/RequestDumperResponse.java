package colesico.framework.undertow.internal;

import io.undertow.UndertowLogger;
import io.undertow.attribute.ExchangeAttribute;
import io.undertow.attribute.ReadOnlyAttributeException;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class RequestDumperResponse implements ExchangeAttribute {
    public static final ExchangeAttribute INSTANCE = new RequestDumperResponse();

    private RequestDumperResponse() {
    }

    public String readAttribute(HttpServerExchange exchange) {
        byte[] data = (byte[]) exchange.getAttachment(ResponseDumperConduit.RESPONSE);
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
        throw new ReadOnlyAttributeException("Stored Response", newValue);
    }

}
