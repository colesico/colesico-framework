package colesico.framework.restlet.teleapi.reader;

import colesico.framework.http.HttpContext;
import colesico.framework.restlet.teleapi.RestletTeleReader;
import colesico.framework.router.RouterContext;

import javax.inject.Provider;

/**
 * Default general purpose reader.
 * Reads values of types for which no specific readers are specified.
 */
abstract public class ValueReader extends RestletTeleReader<Object> {
    public ValueReader(Provider<RouterContext> routerContextProv, Provider<HttpContext> httpContextProv) {
        super(routerContextProv, httpContextProv);
    }
}
