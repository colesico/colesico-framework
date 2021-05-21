package colesico.framework.telehttp.origin;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpCookie;
import colesico.framework.telehttp.Origin;

import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class CookieOrigin implements Origin<String, String> {

    private final Provider<HttpContext> httpContextProv;

    public CookieOrigin(Provider<HttpContext> httpContextProv) {
        this.httpContextProv = httpContextProv;
    }

    @Override
    public String getValue(String key) {
        HttpCookie cookie = httpContextProv.get().getRequest().getCookies().get(key);
        return cookie == null ? null : cookie.getValue();
    }
}
