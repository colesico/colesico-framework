package colesico.framework.weblet.teleapi.origin;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpMethod;
import colesico.framework.http.HttpRequest;
import colesico.framework.router.RouterContext;
import colesico.framework.weblet.teleapi.WebletOrigin;

import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class WebletAutoOrigin implements WebletOrigin {

    private final Provider<HttpContext> httpContextProv;
    private final Provider<RouterContext> routerContextProv;

    public WebletAutoOrigin(Provider<HttpContext> httpContextProv, Provider<RouterContext> routerContextProv) {
        this.httpContextProv = httpContextProv;
        this.routerContextProv = routerContextProv;
    }

    @Override
    public String getString(String name) {
        String value = null;
        HttpRequest httpRequest = httpContextProv.get().getRequest();
        switch (httpRequest.getRequestMethod().getName()) {
            case HttpMethod.GET:
            case HttpMethod.HEAD:
                if (httpRequest.getQueryParameters().hasKey(name)) {
                    return httpRequest.getQueryParameters().get(name);
                }
                return routerContextProv.get().getParameters().get(name);
            case HttpMethod.POST:
            case HttpMethod.PATCH:
            case HttpMethod.DELETE:
            case HttpMethod.PUT:
                if (httpRequest.getPostParameters().hasKey(name)) {
                    return httpRequest.getPostParameters().get(name);
                }
                if (httpRequest.getQueryParameters().hasKey(name)) {
                    return httpRequest.getQueryParameters().get(name);
                }
                RouterContext routerContext = routerContextProv.get();
                return routerContext.getParameters().get(name);
            default:
                return value;
        }
    }
}
