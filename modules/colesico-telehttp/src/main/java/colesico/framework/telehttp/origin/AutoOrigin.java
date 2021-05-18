package colesico.framework.telehttp.origin;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpMethod;
import colesico.framework.http.HttpRequest;
import colesico.framework.router.RouterContext;
import colesico.framework.telehttp.Origin;

import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class AutoOrigin implements Origin<String,String> {

    private final Provider<HttpContext> httpContextProv;
    private final Provider<RouterContext> routerContextProv;

    public AutoOrigin(Provider<HttpContext> httpContextProv, Provider<RouterContext> routerContextProv) {
        this.httpContextProv = httpContextProv;
        this.routerContextProv = routerContextProv;
    }

    @Override
    public String getValue(String key) {
        String value = null;
        HttpRequest httpRequest = httpContextProv.get().getRequest();
        switch (httpRequest.getRequestMethod().getName()) {
            case HttpMethod.GET:
            case HttpMethod.HEAD:
                if (httpRequest.getQueryParameters().hasKey(key)) {
                    return httpRequest.getQueryParameters().get(key);
                }
                return routerContextProv.get().getParameters().get(key);
            case HttpMethod.POST:
            case HttpMethod.PUT:
            case HttpMethod.PATCH:
                if (httpRequest.getPostParameters().hasKey(key)) {
                    return httpRequest.getPostParameters().get(key);
                }
                if (httpRequest.getQueryParameters().hasKey(key)) {
                    return httpRequest.getQueryParameters().get(key);
                }
                RouterContext routerContext = routerContextProv.get();
                if (routerContext.getParameters().containsKey(key)) {
                    return routerContext.getParameters().get(key);
                }
            default:
                return value;
        }
    }
}
