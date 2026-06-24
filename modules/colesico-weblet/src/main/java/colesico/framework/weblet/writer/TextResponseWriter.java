package colesico.framework.weblet.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.telehttp.writer.StringResponseWriter;
import colesico.framework.weblet.response.TextResponse;
import colesico.framework.weblet.WebletTeleWriter;
import colesico.framework.weblet.WebletWriteOptions;
import jakarta.inject.Provider;

public class TextResponseWriter
        extends StringResponseWriter<TextResponse, WebletWriteOptions>
        implements WebletTeleWriter<TextResponse> {

    public static final String DEFAULT_CONTENT_TYPE = "text/plain; charset=utf-8";

    public TextResponseWriter(Provider<HttpResponse> httpResponse) {
        super(httpResponse);
    }

    @Override
    protected String contentType(TextResponse response, WebletWriteOptions options, String defaultValue) {
        return super.contentType(response, options, DEFAULT_CONTENT_TYPE);
    }
}