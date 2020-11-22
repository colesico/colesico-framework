package colesico.framework.telehttp.internal.objectreader;

import colesico.framework.http.HttpContext;
import colesico.framework.router.RouterContext;
import colesico.framework.telehttp.HttpTRContext;
import colesico.framework.telehttp.reader.ObjectReader;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public final class ObjectReaderImpl<C extends HttpTRContext> extends ObjectReader<C> {

    @Inject
    public ObjectReaderImpl(Provider<RouterContext> routerContextProv, Provider<HttpContext> httpContextProv) {
        super(routerContextProv, httpContextProv);
    }

    @Override
    public Object read(C context) {
        // TODO:
        return null;
    }


}
