package colesico.framework.telehttp.response;

import colesico.framework.telehttp.ContentType;

public class BytesResponse extends ValueResponse<byte[]> {
    public BytesResponse(Integer statusCode, ContentType contentType, byte[] value) {
        super(statusCode, contentType, value);
    }
}
