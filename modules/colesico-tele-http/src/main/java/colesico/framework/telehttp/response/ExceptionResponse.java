package colesico.framework.telehttp.response;

import colesico.framework.telehttp.ContentType;

public class ExceptionResponse extends ValueResponse<Exception> {
    public ExceptionResponse(Integer statusCode, ContentType contentType, Exception value) {
        super(statusCode, contentType, value);
    }

    public static ExceptionResponse of(Exception exception) {
        return new ExceptionResponse(null, null, exception);
    }
}
