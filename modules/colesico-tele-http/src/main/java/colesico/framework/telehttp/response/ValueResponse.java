package colesico.framework.telehttp.response;

import colesico.framework.telehttp.MediaType;

/**
 * Response model with any value
 */
public class ValueResponse<V> extends TeleHttpResponse {

    protected final V value;

    public ValueResponse(Integer statusCode, MediaType mediaType, V value) {
        super(statusCode, mediaType);
        this.value = value;
    }

    public V value() {
        return value;
    }

    public static <V> ValueResponse<V> of(V value) {
        return new ValueResponse<>(null, null, value);
    }

    public static <V> ValueResponse<V> of(MediaType mediaType, V value) {
        return new ValueResponse<>(null, mediaType, value);
    }

    public static <V> ValueResponse<V> of(Integer statusCode, V value) {
        return new ValueResponse<>(statusCode, null, value);
    }

    public static <V> ValueResponse<V> of(Integer statusCode, MediaType mediaType, V value) {
        return new ValueResponse<>(statusCode, mediaType, value);
    }
}
