package colesico.framework.telehttp.origin;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpMethod;
import colesico.framework.http.HttpRequest;
import colesico.framework.httprouter.RouterContext;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class AutoOrigin implements Origin {

    private final Provider<HttpContext> httpContextProv;
    private final Provider<RouterContext> routerContextProv;

    public AutoOrigin(Provider<HttpContext> httpContextProv, Provider<RouterContext> routerContextProv) {
        this.httpContextProv = httpContextProv;
        this.routerContextProv = routerContextProv;
    }

    private Iterable<String> routeParam(String name) {
        var value = routerContextProv.get().parameters().get(name);
        return value != null ? List.of(value) : List.of();
    }

    @Override
    public Iterable<String> getStrings(String name) {
        HttpRequest httpRequest = httpContextProv.get().request();
        switch (httpRequest.method().name()) {
            case HttpMethod.GET:
            case HttpMethod.HEAD:
                if (httpRequest.queryParameters().hasKey(name)) {
                    return httpRequest.queryParameters().getAll(name);
                }
                return routeParam(name);
            case HttpMethod.POST:
            case HttpMethod.PATCH:
            case HttpMethod.DELETE:
            case HttpMethod.PUT:
                if (httpRequest.formData().hasKey(name)) {
                    return httpRequest.formData().getAll(name);
                }
                if (httpRequest.queryParameters().hasKey(name)) {
                    return httpRequest.queryParameters().getAll(name);
                }
                return routeParam(name);
            default:
                return List.of();
        }
    }
}
