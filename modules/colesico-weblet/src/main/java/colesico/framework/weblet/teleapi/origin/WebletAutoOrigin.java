package colesico.framework.weblet.teleapi.origin;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpMethod;
import colesico.framework.http.HttpRequest;
import colesico.framework.router.RouterContext;
import colesico.framework.weblet.teleapi.WebletOrigin;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

@Singleton
public class WebletAutoOrigin implements WebletOrigin {

    private final Provider<HttpContext> httpContextProv;
    private final Provider<RouterContext> routerContextProv;

    public WebletAutoOrigin(Provider<HttpContext> httpContextProv, Provider<RouterContext> routerContextProv) {
        this.httpContextProv = httpContextProv;
        this.routerContextProv = routerContextProv;
    }

    @Override
    public String getStrings(String name) {
        String value = null;
        HttpRequest httpRequest = httpContextProv.get().request();
        switch (httpRequest.requestMethod().name()) {
            case HttpMethod.GET:
            case HttpMethod.HEAD:
                if (httpRequest.queryParameters().hasKey(name)) {
                    return httpRequest.queryParameters().get(name);
                }
                return routerContextProv.get().parameters().get(name);
            case HttpMethod.POST:
            case HttpMethod.PATCH:
            case HttpMethod.DELETE:
            case HttpMethod.PUT:
                if (httpRequest.postParameters().hasKey(name)) {
                    return httpRequest.postParameters().get(name);
                }
                if (httpRequest.queryParameters().hasKey(name)) {
                    return httpRequest.queryParameters().get(name);
                }
                RouterContext routerContext = routerContextProv.get();
                return routerContext.parameters().get(name);
            default:
                return value;
        }
    }
}
