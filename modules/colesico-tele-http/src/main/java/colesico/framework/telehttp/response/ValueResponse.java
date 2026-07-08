package colesico.framework.telehttp.response;

import colesico.framework.http.HttpCookie;
import colesico.framework.telehttp.ContentType;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * General response model with a value
 */
abstract public class ValueResponse<V> extends TeleHttpResponse {

    protected final V value;

    public ValueResponse(Integer statusCode, ContentType contentType, Map<String, List<String>> headers, Set<HttpCookie> cookies, V value) {
        super(statusCode, contentType, headers, cookies);
        this.value = value;
    }

    public V value() {
        return value;
    }

    @Override
    public String toString() {
        return "ValueResponse{" +
                "statusCode=" + statusCode +
                ", value=" + value +
                '}';
    }

    abstract public static class Builder<V, R extends ValueResponse<V>, B extends Builder<V, R, B>>
            extends TeleHttpResponse.Builder<R, B> {

        protected final V value;

        public Builder(V value) {
            this.value = value;
        }
    }
}
