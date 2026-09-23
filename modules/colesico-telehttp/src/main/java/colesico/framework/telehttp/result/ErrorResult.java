package colesico.framework.telehttp.result;

import colesico.framework.http.HttpCookie;
import colesico.framework.telehttp.ContentType;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * General purpose value result implementation
 */
public class ErrorResult<V>
        extends AbstractHttpResult
        implements ValueHttpResult<V> {

    protected final V value;

    public ErrorResult(Integer status, ContentType contentType, Map<String, List<String>> headers, Set<HttpCookie> cookies, V value) {
        super(status, contentType, headers, cookies);
        this.value = value;
    }

    public V value() {
        return value;
    }

    public static <V, R extends ErrorResult<V>, B extends Builder<V, R, B>> Builder<V, R, B> value(V value) {
        return new Builder<>(value);
    }

    @Override
    public String toString() {
        return "ObjectResult{" +
                "status=" + status +
                ", value=" + value +
                '}';
    }

    public static class Builder<V, R extends ErrorResult<V>, B extends Builder<V, R, B>>
            extends AbstractHttpResult.Builder<R, B> {

        protected V value;

        public B value(V value) {
            this.value = value;
            return self();
        }

        public Builder(V value) {
            this.value = value;
        }

        @Override
        @SuppressWarnings("unchecked")
        public R build() {
            return (R) new ErrorResult<>(status, contentType, headers, cookies, value);
        }

        @Override
        @SuppressWarnings("unchecked")
        protected B self() {
            return (B) this;
        }
    }
}
