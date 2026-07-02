package colesico.framework.telehttp.response;

import colesico.framework.telehttp.ContentType;

public class ObjectResponse extends ValueResponse<Object> {
    public ObjectResponse(Integer statusCode, ContentType contentType, Object value) {
        super(statusCode, contentType, value);
    }

    public static ObjectResponse of(Object value) {
        return new ObjectResponse(null, null, value);
    }
}
