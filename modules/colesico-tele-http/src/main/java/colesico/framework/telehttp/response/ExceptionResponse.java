package colesico.framework.telehttp.response;

import colesico.framework.telehttp.MediaType;

public class ExceptionResponse extends ValueResponse<Exception> {
    public ExceptionResponse(Integer statusCode, MediaType mediaType, Exception value) {
        super(statusCode, mediaType, value);
    }
}
