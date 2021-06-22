package colesico.framework.telehttp.origin;

import colesico.framework.http.HttpContext;
import colesico.framework.telehttp.Origin;

import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class HeaderOrigin implements Origin {

    private final Provider<HttpContext> httpContextProv;

    public HeaderOrigin(Provider<HttpContext> httpContextProv) {
        this.httpContextProv = httpContextProv;
    }

    @Override
    public String getString(String name) {
       return httpContextProv.get().getRequest().getHeaders().get(name);
    }
}
