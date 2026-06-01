package colesico.framework.undertow.internal;

import colesico.framework.http.HttpCookieFactory;
import colesico.framework.http.HttpCookie;
import io.undertow.server.handlers.Cookie;
import io.undertow.server.handlers.CookieImpl;

import jakarta.inject.Singleton;

@Singleton
public class UndertowCookieFactory implements HttpCookieFactory {

    @Override
    public HttpCookie create(String name, String value) {
        Cookie c = new CookieImpl(name, value);
        c.setPath(HttpCookie.DEFAULT_PATH);
        return new UndertowCookie(c);
    }

}
