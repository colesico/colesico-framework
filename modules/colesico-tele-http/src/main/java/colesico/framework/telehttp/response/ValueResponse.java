package colesico.framework.telehttp.response;

import colesico.framework.telehttp.MediaType;

/**
 * General response model with a value
 */
abstract class ValueResponse<V> extends TeleHttpResponse {

    protected final V value;

    public ValueResponse(Integer statusCode, MediaType mediaType, V value) {
        super(statusCode, mediaType);
        this.value = value;
    }

    public V value() {
        return value;
    }

}
