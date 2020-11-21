package colesico.framework.telehttp.reader.objreader;

import colesico.framework.http.HttpContext;
import colesico.framework.router.RouterContext;
import colesico.framework.telehttp.HttpTRContext;
import colesico.framework.telehttp.HttpTeleReader;

import javax.inject.Provider;

/**
 * Performs custom object reading with reflection.
 * Object fields values are reading with appropriate http-readers if the field type can be reade by tye reader.
 * Otherwise field read as nested custom object
 */
public final class ObjectReader<C extends HttpTRContext> extends HttpTeleReader<Object, C> {
    public ObjectReader(Provider<RouterContext> routerContextProv, Provider<HttpContext> httpContextProv) {
        super(routerContextProv, httpContextProv);
    }

    @Override
    public Object read(C context) {
        //TODO!
        return null;
    }
}
