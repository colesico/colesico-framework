package colesico.framework.weblet.writer;

import colesico.framework.http.HttpResponse;
import colesico.framework.telehttp.writer.HttpResultWriter;
import colesico.framework.weblet.WebletWriteOptions;
import colesico.framework.weblet.response.ViewResponse;
import colesico.framework.weblet.WebletWriter;
import jakarta.inject.Provider;

/**
 * This writer shod be implemented by concrete view renderer  (for different templating support)
 */
abstract public class ViewWriter
        extends HttpResultWriter<ViewResponse, WebletWriteOptions>
        implements WebletWriter<ViewResponse> {

    public ViewWriter(Provider<HttpResponse> httpResponse) {
        super(httpResponse);
    }
}
