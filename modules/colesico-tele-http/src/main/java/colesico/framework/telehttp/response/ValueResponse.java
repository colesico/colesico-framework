package colesico.framework.telehttp.response;

import colesico.framework.telehttp.ContentType;

/**
 * General response model with a value
 */
abstract public class ValueResponse<V> extends TeleHttpResponse {

    protected final V value;

    public ValueResponse(Integer statusCode, ContentType contentType, V value) {
        super(statusCode, contentType);
        this.value = value;
    }

    public V value() {
        return value;
    }

}
