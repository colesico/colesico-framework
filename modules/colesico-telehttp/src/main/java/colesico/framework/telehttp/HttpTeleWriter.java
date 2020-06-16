package colesico.framework.telehttp;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpRequest;
import colesico.framework.http.HttpResponse;
import colesico.framework.teleapi.TeleWriter;

import javax.inject.Provider;

/**
 * Basic tel-writer for interaction over http
 * @param <V> value type
 * @param <C> writing context
 */
abstract public class HttpTeleWriter<V, C extends HttpTWContext> implements TeleWriter<V, C> {

    protected final Provider<HttpContext> httpContextProv;

    public HttpTeleWriter(Provider<HttpContext> httpContextProv) {
        this.httpContextProv = httpContextProv;
    }

    /**
     * Constructor for proxies
     */
    public HttpTeleWriter(HttpTeleWriter writer) {
        this.httpContextProv = writer.httpContextProv;
    }

    protected final HttpRequest getRequest() {
        return httpContextProv.get().getRequest();
    }

    protected final HttpResponse getResponse() {
        return httpContextProv.get().getResponse();
    }

}
