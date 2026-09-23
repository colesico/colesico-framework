package colesico.framework.telehttp.result;

import colesico.framework.http.HttpCookie;
import colesico.framework.telehttp.ContentType;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class BytesResult extends ValueResult<byte[]> {

    protected BytesResult(Integer status, ContentType contentType, Map<String, List<String>> headers, Set<HttpCookie> cookies, byte[] value) {
        super(status, contentType, headers, cookies, value);
    }

    public static BytesResult.Builder value(byte[] value) {
        return new BytesResult.Builder(value);
    }

    public static class Builder extends ValueResult.Builder<byte[], BytesResult, BytesResult.Builder> {

        public Builder(byte[] value) {
            super(value);
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public BytesResult build() {
            return new BytesResult(status, contentType, headers, cookies, value);
        }
    }
}
