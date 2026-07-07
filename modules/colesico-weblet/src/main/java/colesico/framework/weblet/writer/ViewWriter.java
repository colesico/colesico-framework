package colesico.framework.weblet.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.telehttp.writer.TeleHttpResponseWriter;
import colesico.framework.weblet.WebletWriteOptions;
import colesico.framework.weblet.response.ViewResponse;
import colesico.framework.weblet.WebletWriter;
import jakarta.inject.Provider;

/**
 * This writer shod be implemented by concrete view renderer  (for different templating support)
 */
abstract public class ViewWriter
        extends TeleHttpResponseWriter<ViewResponse, WebletWriteOptions>
        implements WebletWriter<ViewResponse> {

    public ViewWriter(Provider<HttpResponse> httpResponse) {
        super(httpResponse);
    }
}
