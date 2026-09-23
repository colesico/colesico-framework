package colesico.framework.telehttp.result;

import colesico.framework.http.HttpCookie;
import colesico.framework.telehttp.ContentType;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class StringResult extends ObjectResult<String> {

    protected StringResult(Integer status, ContentType contentType, Map<String, List<String>> headers, Set<HttpCookie> cookies, String value) {
        super(status, contentType, headers, cookies, value);
    }

    /**
     * Text/Plain response
     */
    public static Builder text(String value) {
        return value(value).contentType(ContentType.TEXT_PLAIN);
    }

    public static Builder text() {
        return of().contentType(ContentType.TEXT_PLAIN);
    }

    /**
     * Text/Html response
     */
    public static Builder html(String value) {
        return value(value).contentType(ContentType.TEXT_HTML);
    }

    public static Builder html() {
        return of().contentType(ContentType.TEXT_HTML);
    }

    public static Builder value(String value) {
        return new Builder(value);
    }

    public static Builder of() {
        return new Builder(null);
    }

    public static class Builder extends ObjectResult.Builder<String, StringResult, Builder> {

        public Builder(String value) {
            super(value);
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public StringResult build() {
            return new StringResult(status, contentType, headers, cookies, value);
        }
    }
}
