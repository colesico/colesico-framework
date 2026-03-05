package colesico.framework.telehttp.origin;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpCookie;
import colesico.framework.http.MultiValue;
import colesico.framework.telehttp.Origin;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Singleton
public class CookieOrigin implements Origin {

    private final Provider<HttpContext> httpContextProv;

    public CookieOrigin(Provider<HttpContext> httpContextProv) {
        this.httpContextProv = httpContextProv;
    }

    @Override
    public Collection<String> getStrings(String name) {
        List<String> result = new ArrayList<>();
        MultiValue<HttpCookie> cookies = httpContextProv.get().getRequest().getCookies().getAll(name);
        if (cookies != null) {
            cookies.iterator().forEachRemaining(c -> result.add(c.getValue()));
        }
        return result;
    }
}
