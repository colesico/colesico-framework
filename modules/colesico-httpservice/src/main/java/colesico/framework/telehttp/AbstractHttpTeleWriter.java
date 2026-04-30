package colesico.framework.telehttp;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpRequest;
import colesico.framework.http.HttpResponse;
import colesico.framework.teleapi.dataport.TeleWriter;

import jakarta.inject.Provider;

abstract public class AbstractHttpTeleWriter<V, C extends HttpTWContext<?, ?>> implements HttpTeleWriter<V, C> {

    protected final Provider<HttpContext> httpContextProv;

    /**
     * Constructor for injection
     */
    public AbstractHttpTeleWriter(Provider<HttpContext> httpContextProv) {
        this.httpContextProv = httpContextProv;
    }

    /**
     * Constructor for proxies
     */
    public AbstractHttpTeleWriter(AbstractHttpTeleWriter<?, ?> writer) {
        this.httpContextProv = writer.httpContextProv;
    }

    protected final HttpRequest request() {
        return httpContextProv.get().request();
    }

    protected final HttpResponse response() {
        return httpContextProv.get().response();
    }

}
