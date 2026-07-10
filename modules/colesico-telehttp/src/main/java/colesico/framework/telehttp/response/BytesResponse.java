package colesico.framework.telehttp.response;

import colesico.framework.http.HttpCookie;
import colesico.framework.telehttp.ContentType;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class BytesResponse extends ValueResponse<byte[]> {

    protected BytesResponse(Integer statusCode, ContentType contentType, Map<String, List<String>> headers, Set<HttpCookie> cookies, byte[] value) {
        super(statusCode, contentType, headers, cookies, value);
    }

    public static BytesResponse.Builder value(byte[] value) {
        return new BytesResponse.Builder(value);
    }

    public static class Builder extends ValueResponse.Builder<byte[], BytesResponse, BytesResponse.Builder> {

        public Builder(byte[] value) {
            super(value);
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public BytesResponse build() {
            return new BytesResponse(statusCode, contentType, headers, cookies, value);
        }
    }
}
