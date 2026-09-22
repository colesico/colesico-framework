package colesico.framework.telehttp.result;

import colesico.framework.http.HttpCookie;
import colesico.framework.telehttp.ContentType;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ObjectResult extends AbstractValueResult<Object> {

    protected ObjectResult(Integer status, ContentType contentType, Map<String, List<String>> headers, Set<HttpCookie> cookies, Object value) {
        super(status, contentType, headers, cookies, value);
    }

    public static Builder value(Object value) {
        return new Builder(value);
    }

    public static class Builder extends AbstractValueResult.Builder<Object, ObjectResult, ObjectResult.Builder> {

        public Builder(Object value) {
            super(value);
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public ObjectResult build() {
            return new ObjectResult(status, contentType, headers, cookies, value);
        }
    }

}
