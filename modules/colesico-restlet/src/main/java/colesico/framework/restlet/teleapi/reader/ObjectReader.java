package colesico.framework.restlet.teleapi.reader;

import colesico.framework.http.HttpContext;
import colesico.framework.restlet.teleapi.RestletTeleReader;
import colesico.framework.router.RouterContext;

import javax.inject.Provider;

abstract public class ObjectReader extends RestletTeleReader<Object> {
    public ObjectReader(Provider<RouterContext> routerContextProv, Provider<HttpContext> httpContextProv) {
        super(routerContextProv, httpContextProv);
    }
}
