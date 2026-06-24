package colesico.framework.weblet.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.telehttp.writer.StringResponseWriter;
import colesico.framework.weblet.response.HtmlResponse;
import colesico.framework.weblet.WebletTeleWriter;
import colesico.framework.weblet.WebletWriteOptions;
import jakarta.inject.Provider;

public class HtmlResponseWriter
        extends StringResponseWriter<HtmlResponse, WebletWriteOptions>
        implements WebletTeleWriter<HtmlResponse> {

    public static final String DEFAULT_CONTENT_TYPE = "text/html; charset=utf-8";

    public HtmlResponseWriter(Provider<HttpResponse> httpResponse) {
        super(httpResponse);
    }

    @Override
    protected String contentType(HtmlResponse response, WebletWriteOptions options, String defaultValue) {
        return super.contentType(response, options, DEFAULT_CONTENT_TYPE);
    }
}
