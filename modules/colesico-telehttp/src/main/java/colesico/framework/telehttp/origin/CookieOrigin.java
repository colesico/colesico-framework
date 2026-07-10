package colesico.framework.telehttp.origin;

import colesico.framework.http.HttpCookie;
import colesico.framework.http.HttpRequest;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Singleton
public class CookieOrigin implements Origin {

    protected final Provider<HttpRequest> httpRequest;

    public CookieOrigin(Provider<HttpRequest> httpRequest) {
        this.httpRequest = httpRequest;
    }

    @Override
    public Collection<String> getStrings(String name) {
        final List<String> result = new ArrayList<>();
        var cookies = httpRequest.get().cookies().getAll(name);
        if (cookies != null) {
            cookies.iterator().forEachRemaining(c -> result.add(c.value()));
        }
        return result;
    }
}
