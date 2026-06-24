package colesico.framework.weblet.teleapi.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.telehttp.writer.StringResponseWriter;
import colesico.framework.weblet.teleapi.response.TextResponse;
import colesico.framework.weblet.teleapi.WebletTeleWriter;
import colesico.framework.weblet.teleapi.WebletWriteOptions;
import jakarta.inject.Provider;

public class TextResponseWriter
        extends StringResponseWriter<TextResponse, WebletWriteOptions>
        implements WebletTeleWriter<TextResponse> {

    public static final String DEFAULT_CONTENT_TYPE = "text/plain; charset=utf-8";

    public TextResponseWriter(Provider<HttpResponse> httpResponse) {
        super(httpResponse);
    }

    @Override
    protected String contentType(TextResponse value, WebletWriteOptions options) {
        return DEFAULT_CONTENT_TYPE;
    }
}