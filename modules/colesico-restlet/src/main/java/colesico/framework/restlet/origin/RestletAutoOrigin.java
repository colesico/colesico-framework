package colesico.framework.restlet.origin;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpMethod;
import colesico.framework.http.HttpRequest;
import colesico.framework.restlet.RestletOrigin;
import colesico.framework.router.RouterContext;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class RestletAutoOrigin implements RestletOrigin {

    private final Provider<HttpContext> httpContextProv;
    private final Provider<RouterContext> routerContextProv;

    public RestletAutoOrigin(Provider<HttpContext> httpContextProv, Provider<RouterContext> routerContextProv) {
        this.httpContextProv = httpContextProv;
        this.routerContextProv = routerContextProv;
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
                return List.of(routerContextProv.get().parameters().get(name));
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
                RouterContext routerContext = routerContextProv.get();
                return List.of(routerContext.parameters().get(name));
            default:
                return List.of();
        }
    }
}
