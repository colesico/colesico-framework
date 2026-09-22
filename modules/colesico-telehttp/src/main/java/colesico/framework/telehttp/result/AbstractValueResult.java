package colesico.framework.telehttp.result;

import colesico.framework.http.HttpCookie;
import colesico.framework.telehttp.ContentType;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * General response model with a value
 */
abstract public class AbstractValueResult<V>
        extends AbstractHttpResult
        implements ValueResult<V> {

    protected final V value;

    public AbstractValueResult(Integer status, ContentType contentType, Map<String, List<String>> headers, Set<HttpCookie> cookies, V value) {
        super(status, contentType, headers, cookies);
        this.value = value;
    }

    public V value() {
        return value;
    }

    @Override
    public String toString() {
        return "ValueResult{" +
                "status=" + status +
                ", value=" + value +
                '}';
    }

    abstract public static class Builder<V, R extends AbstractValueResult<V>, B extends Builder<V, R, B>>
            extends AbstractHttpResult.Builder<R, B> {

        protected V value;

        public B value(V value) {
            this.value = value;
            return self();
        }

        public Builder(V value) {
            this.value = value;
        }

    }
}
