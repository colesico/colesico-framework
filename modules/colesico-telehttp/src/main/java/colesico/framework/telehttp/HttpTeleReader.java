package colesico.framework.telehttp;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpRequest;
import colesico.framework.http.HttpResponse;
import colesico.framework.router.RouterContext;
import colesico.framework.teleapi.TeleReader;

import javax.inject.Provider;

/**
 * Basic tele-reader for reading param vales from remote client via http
 * @param <V>
 * @param <C>
 */
abstract public class HttpTeleReader<V, C extends HttpTRContext> implements TeleReader<V, C> {

    protected final Provider<RouterContext> routerContextProv;
    protected final Provider<HttpContext> httpContextProv;

    /**
     * CConstructor for injections
     */
    public HttpTeleReader(Provider<RouterContext> routerContextProv, Provider<HttpContext> httpContextProv) {
        this.routerContextProv = routerContextProv;
        this.httpContextProv = httpContextProv;
    }

    /**
     * Constructor for proxies
     */
    public HttpTeleReader(HttpTeleReader reader) {
        this.routerContextProv = reader.routerContextProv;
        this.httpContextProv = reader.routerContextProv;
    }

    protected final HttpRequest getRequest() {
        return httpContextProv.get().getRequest();
    }

    protected final HttpResponse getResponse() {
        return httpContextProv.get().getResponse();
    }

    protected final RouterContext getRouterContext() {
        return routerContextProv.get();
    }

}
