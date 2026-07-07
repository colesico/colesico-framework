package colesico.framework.telehttp.response;

import colesico.framework.http.HttpCookie;
import colesico.framework.telehttp.ContentType;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class StringResponse extends ValueResponse<String> {

    protected StringResponse(Integer statusCode, ContentType contentType, Map<String, List<String>> headers, Set<HttpCookie> cookies, String value) {
        super(statusCode, contentType, headers, cookies, value);
    }

    /**
     * Text/Plain response
     */
    public static Builder text(String value) {
        return value(value).contentType(ContentType.TEXT_PLAIN);
    }

    /**
     * Text/Html response
     */
    public static Builder html(String value) {
        return value(value).contentType(ContentType.TEXT_HTML);
    }

    public static Builder value(String value) {
        return new Builder(value);
    }

    public static class Builder extends ValueResponse.Builder<String, StringResponse, Builder> {

        public Builder(String value) {
            super(value);
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public StringResponse build() {
            return new StringResponse(statusCode, contentType, headers, cookies, value);
        }
    }
}
