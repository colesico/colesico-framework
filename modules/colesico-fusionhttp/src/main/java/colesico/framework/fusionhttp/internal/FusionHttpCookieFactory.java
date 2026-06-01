package colesico.framework.fusionhttp.internal;

import colesico.framework.http.HttpCookieFactory;
import colesico.framework.http.HttpCookie;
import io.fusionauth.http.Cookie;
import jakarta.inject.Singleton;

@Singleton
public class FusionHttpCookieFactory implements HttpCookieFactory {
    @Override
    public HttpCookie create(String name, String value) {
        return new FusionHttpCookie(new Cookie(name, value));
    }
}
