package colesico.framework.telehttp.response;

import colesico.framework.http.HttpCookie;
import colesico.framework.telehttp.ContentType;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExceptionResponse extends ValueResponse<Exception> {
    protected ExceptionResponse(Integer statusCode, ContentType contentType, Map<String, List<String>> headers, Set<HttpCookie> cookies, Exception value) {
        super(statusCode, contentType, headers, cookies, value);
    }

    public static ExceptionResponse.Builder exception(Exception exception) {
        return new ExceptionResponse.Builder(exception);
    }

    public static class Builder extends ValueResponse.Builder<Exception, ExceptionResponse, ExceptionResponse.Builder> {

        public Builder(Exception ex) {
            super(ex);
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public ExceptionResponse build() {
            return new ExceptionResponse(statusCode, contentType, headers, cookies, value);
        }
    }
}
