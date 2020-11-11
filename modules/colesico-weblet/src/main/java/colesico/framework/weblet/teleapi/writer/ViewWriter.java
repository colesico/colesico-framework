package colesico.framework.weblet.teleapi.writer;

import colesico.framework.http.HttpContext;
import colesico.framework.telehttp.HttpTeleWriter;
import colesico.framework.weblet.ViewResponse;
import colesico.framework.weblet.teleapi.WebletTeleWriter;

import javax.inject.Provider;

/**
 * This writer shod be implemented by concrete view renderer  (for different templating support)
 */
abstract public class ViewWriter extends WebletTeleWriter<ViewResponse> {

    public ViewWriter(Provider<HttpContext> httpContextProv) {
        super(httpContextProv);
    }

    public ViewWriter(HttpTeleWriter writer) {
        super(writer);
    }
}
