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
public class HeaderOrigin implements Origin {

    private final Provider<HttpContext> httpContextProv;

    public HeaderOrigin(Provider<HttpContext> httpContextProv) {
        this.httpContextProv = httpContextProv;
    }

    @Override
    public Collection<String> getStrings(String name) {
        List<String> result = new ArrayList<>();
        MultiValue<String> headers = httpContextProv.get().getRequest().getHeaders().getAll(name);
        if (headers != null) {
            headers.iterator().forEachRemaining(result::add);
        }
        return result;
    }
}
