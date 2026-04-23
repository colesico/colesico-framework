package colesico.framework.restlet.teleapi.origin;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpRequest;
import colesico.framework.restlet.teleapi.RestletOrigin;
import colesico.framework.router.RouterContext;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

@Singleton
public class RestletAutoOrigin implements RestletOrigin {

    private final Provider<HttpContext> httpContextProv;
    private final Provider<RouterContext> routerContextProv;

    public RestletAutoOrigin(Provider<HttpContext> httpContextProv, Provider<RouterContext> routerContextProv) {
        this.httpContextProv = httpContextProv;
        this.routerContextProv = routerContextProv;
    }

    @Override
    public String getStrings(String name) {
        HttpRequest httpRequest = httpContextProv.get().request();
        if (httpRequest.queryParameters().hasKey(name)) {
            return httpRequest.queryParameters().get(name);
        }
        RouterContext routerContext = routerContextProv.get();

        // return param value  or null
        return routerContext.parameters().get(name);
    }
}
