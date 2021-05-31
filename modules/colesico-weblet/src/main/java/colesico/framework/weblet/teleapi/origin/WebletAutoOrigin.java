package colesico.framework.weblet.teleapi.origin;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpMethod;
import colesico.framework.http.HttpRequest;
import colesico.framework.router.RouterContext;
import colesico.framework.weblet.teleapi.WebletOrigin;

import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class WebletAutoOrigin implements WebletOrigin<String, String> {

    private final Provider<HttpContext> httpContextProv;
    private final Provider<RouterContext> routerContextProv;

    public WebletAutoOrigin(Provider<HttpContext> httpContextProv, Provider<RouterContext> routerContextProv) {
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
                // return param value or null
                return routerContextProv.get().getParameters().get(key);
            case HttpMethod.POST:
            case HttpMethod.PATCH:
            case HttpMethod.DELETE:
            case HttpMethod.PUT:
                if (httpRequest.getPostParameters().hasKey(key)) {
                    return httpRequest.getPostParameters().get(key);
                }
                if (httpRequest.getQueryParameters().hasKey(key)) {
                    return httpRequest.getQueryParameters().get(key);
                }
                RouterContext routerContext = routerContextProv.get();
                // return param value or null
                routerContext.getParameters().get(key);
            default:
                return value;
        }
    }
}
