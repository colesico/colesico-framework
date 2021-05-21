package colesico.framework.telehttp.origin;

import colesico.framework.http.HttpContext;
import colesico.framework.telehttp.Origin;

import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class HeaderOrigin implements Origin<String,String> {

    private final Provider<HttpContext> httpContextProv;

    public HeaderOrigin(Provider<HttpContext> httpContextProv) {
        this.httpContextProv = httpContextProv;
    }

    @Override
    public String getValue(String key) {
       return httpContextProv.get().getRequest().getHeaders().get(key);
    }
}
