package colesico.framework.telehttp.response;

import colesico.framework.telehttp.MediaType;

public class BytesResponse extends ValueResponse<byte[]> {
    public BytesResponse(Integer statusCode, MediaType mediaType, byte[] value) {
        super(statusCode, mediaType, value);
    }
}
