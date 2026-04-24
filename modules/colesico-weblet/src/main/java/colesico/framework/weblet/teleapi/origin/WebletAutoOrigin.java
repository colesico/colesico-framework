package colesico.framework.weblet.teleapi.origin;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpMethod;
import colesico.framework.http.HttpRequest;
import colesico.framework.router.RouterContext;
import colesico.framework.weblet.teleapi.WebletOrigin;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.util.Collection;
import java.util.List;

@Singleton
public class WebletAutoOrigin implements WebletOrigin {

    private final Provider<HttpContext> httpContextProv;
    private final Provider<RouterContext> routerContextProv;

    public WebletAutoOrigin(Provider<HttpContext> httpContextProv, Provider<RouterContext> routerContextProv) {
        this.httpContextProv = httpContextProv;
        this.routerContextProv = routerContextProv;
    }

    @Override
    public Iterable<String> getStrings(String name) {
        String value = null;
        HttpRequest httpRequest = httpContextProv.get().request();
        switch (httpRequest.requestMethod().name()) {
            case HttpMethod.GET:
            case HttpMethod.HEAD:
                if (httpRequest.queryParameters().hasKey(name)) {
                    return httpRequest.queryParameters().getAll(name);
                }
                return List.of(routerContextProv.get().parameters().get(name));
            case HttpMethod.POST:
            case HttpMethod.PATCH:
            case HttpMethod.DELETE:
            case HttpMethod.PUT:
                if (httpRequest.postParameters().hasKey(name)) {
                    return httpRequest.postParameters().getAll(name);
                }
                if (httpRequest.queryParameters().hasKey(name)) {
                    return httpRequest.queryParameters().getAll(name);
                }
                RouterContext routerContext = routerContextProv.get();
                return List.of(routerContext.parameters().get(name));
            default:
                return List.of(value);
        }
    }
}
