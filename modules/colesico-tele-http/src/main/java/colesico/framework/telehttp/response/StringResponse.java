package colesico.framework.telehttp.response;

import colesico.framework.telehttp.MediaType;

public final class StringResponse extends ValueResponse<String> {
    public StringResponse(Integer statusCode, MediaType mediaType, String value) {
        super(statusCode, mediaType, value);
    }
}
