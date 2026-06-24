package colesico.framework.weblet.teleapi.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.telehttp.writer.StringResponseWriter;
import colesico.framework.weblet.response.HtmlResponse;
import colesico.framework.weblet.teleapi.WebletTeleWriter;
import colesico.framework.weblet.teleapi.WebletWriteOptions;
import jakarta.inject.Provider;

public class HtmlResponseWriter
        extends StringResponseWriter<HtmlResponse, WebletWriteOptions>
        implements WebletTeleWriter<HtmlResponse> {

    public static final String DEFAULT_CONTENT_TYPE = "text/html; charset=utf-8";

    public HtmlResponseWriter(Provider<HttpResponse> httpResponse) {
        super(httpResponse);
    }

    @Override
    protected String defaultContentType(HtmlResponse value, WebletWriteOptions options) {
        return DEFAULT_CONTENT_TYPE;
    }
}
