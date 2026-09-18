package colesico.framework.telehttp.response;

import colesico.framework.http.HttpCookie;
import colesico.framework.telehttp.ContentType;
import colesico.framework.telehttp.HttpTeleError;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExceptionResponse extends ValueResponse<Exception> {

    protected final String errorCode;

    protected final Object details;

    public ExceptionResponse(Integer statusCode, ContentType contentType, Map<String, List<String>> headers, Set<HttpCookie> cookies, Exception value, String errorCode, Object details) {
        super(statusCode, contentType, headers, cookies, value);
        this.errorCode = errorCode;
        this.details = details;
    }

    public static ExceptionResponse.Builder exception(Exception exception) {
        return new ExceptionResponse.Builder(exception);
    }

    @Override
    public String toString() {
        return "ExceptionResponse{" +
                "exception=" + value +
                ", statusCode=" + statusCode +
                '}';
    }

    public static class Builder extends ValueResponse.Builder<Exception, ExceptionResponse, ExceptionResponse.Builder> {

        protected String errorCode;

        protected Object details;

        public Builder(Exception ex) {
            super(ex);
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public ExceptionResponse build() {
            return new ExceptionResponse(statusCode, contentType, headers, cookies, value, errorCode, details);
        }
    }
}
