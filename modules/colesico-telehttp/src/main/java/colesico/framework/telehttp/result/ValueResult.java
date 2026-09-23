package colesico.framework.telehttp.result;

import colesico.framework.http.HttpCookie;
import colesico.framework.telehttp.ContentType;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * General purpose value result
 */
public class ValueResult<V>
        extends AbstractHttpResult
        implements ValueHttpResult<V> {

    protected final V value;

    public ValueResult(Integer status, ContentType contentType, Map<String, List<String>> headers, Set<HttpCookie> cookies, V value) {
        super(status, contentType, headers, cookies);
        this.value = value;
    }

    public V value() {
        return value;
    }

    public static <V, R extends ValueResult<V>, B extends Builder<V, R, B>> Builder<V, R, B> value(V value) {
        return new Builder<>(value);
    }

    @Override
    public String toString() {
        return "ValueResult{" +
                "status=" + status +
                ", value=" + value +
                '}';
    }

    public static class Builder<V, R extends ValueResult<V>, B extends Builder<V, R, B>>
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
            return (R) new ValueResult<>(status, contentType, headers, cookies, value);
        }

        @Override
        @SuppressWarnings("unchecked")
        protected B self() {
            return (B) this;
        }
    }
}
