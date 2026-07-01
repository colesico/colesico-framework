package colesico.framework.telehttp.response;

import colesico.framework.telehttp.MediaType;

public class ObjectResponse extends ValueResponse<Object> {
    public ObjectResponse(Integer statusCode, MediaType mediaType, Object value) {
        super(statusCode, mediaType, value);
    }

    public static ObjectResponse of(Object value) {
        return new ObjectResponse(null, null, value);
    }
}
