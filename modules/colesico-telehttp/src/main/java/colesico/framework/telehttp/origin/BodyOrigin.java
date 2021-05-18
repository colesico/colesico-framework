package colesico.framework.telehttp.origin;

import colesico.framework.http.HttpContext;
import colesico.framework.telehttp.Origin;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.InputStream;

@Singleton
public class BodyOrigin implements Origin<Object, InputStream> {

    private final Provider<HttpContext> httpContextProv;

    public BodyOrigin(Provider<HttpContext> httpContextProv) {
        this.httpContextProv = httpContextProv;
    }

    @Override
    public InputStream getValue(Object key) {
        return httpContextProv.get().getRequest().getInputStream();
    }
}
