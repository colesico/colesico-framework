package colesico.framework.telehttp.response;

import colesico.framework.http.HttpCookie;
import colesico.framework.telehttp.ContentType;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ObjectResponse extends ValueResponse<Object> {

    protected ObjectResponse(Integer statusCode, ContentType contentType, Map<String, List<String>> headers, Set<HttpCookie> cookies, Object value) {
        super(statusCode, contentType, headers, cookies, value);
    }

    public static Builder value(Object value) {
        return new Builder(value);
    }

    public static class Builder extends ValueResponse.Builder<Object, ObjectResponse, ObjectResponse.Builder> {

        public Builder(Object value) {
            super(value);
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public ObjectResponse build() {
            return new ObjectResponse(statusCode, contentType, headers, cookies, value);
        }
    }

}
