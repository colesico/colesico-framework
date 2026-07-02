package colesico.framework.weblet.response;

import colesico.framework.http.HttpCookie;
import colesico.framework.telehttp.ContentType;
import colesico.framework.telehttp.response.StringResponse;
import colesico.framework.telehttp.response.ValueResponse;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Simple HTML response
 */
public final class HtmlResponse extends StringResponse {

    protected HtmlResponse(Integer statusCode, ContentType contentType, Map<String, List<String>> headers, Set<HttpCookie> cookies, String value) {
        super(statusCode, contentType, headers, cookies, value);
    }

    public static HtmlResponse.Builder html(String value) {
        return new HtmlResponse.Builder(value);
    }

    public static class Builder extends ValueResponse.Builder<String, HtmlResponse, HtmlResponse.Builder> {

        private Charset charset = StandardCharsets.UTF_8;

        public Builder(String value) {
            super(value);
        }

        public Builder charset(Charset charset) {
            this.charset = charset;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public HtmlResponse build() {
            if (this.contentType == null) {
                this.contentType = ContentType.of("text/html", charset);
            }
            return new HtmlResponse(statusCode, contentType, headers, cookies, value);
        }
    }
}
