package colesico.framework.fusionhttp.internal;

import colesico.framework.http.HttpCookie;
import io.fusionauth.http.Cookie;

import java.time.Instant;
import java.time.ZoneOffset;

public class FusionHttpCookie implements HttpCookie {

    private final Cookie cookie;

    public FusionHttpCookie(Cookie cookie) {
        this.cookie = cookie;
    }

    @Override
    public String name() {
        return cookie.getName();
    }

    @Override
    public String value() {
        return cookie.getValue();
    }

    @Override
    public String domain() {
        return cookie.getDomain();
    }

    @Override
    public String path() {
        return cookie.getPath();
    }

    @Override
    public Long maxAge() {
        return cookie.getMaxAge();
    }

    @Override
    public Instant expires() {
        return cookie.getExpires().toInstant();
    }

    @Override
    public Boolean secure() {
        return cookie.isSecure();
    }

    @Override
    public SameSite sameSite() {
        return switch (cookie.getSameSite()) {
            case Lax -> SameSite.LAX;
            case Strict -> SameSite.STRICT;
            case None -> SameSite.NONE;
        };
    }

    @Override
    public Boolean httpOnly() {
        return cookie.isHttpOnly();
    }

    @Override
    public HttpCookie setValue(String value) {
        cookie.setValue(value);
        return this;
    }

    @Override
    public HttpCookie setDomain(String domain) {
        cookie.setDomain(domain);
        return this;
    }

    @Override
    public HttpCookie setPath(String path) {
        cookie.setPath(path);
        return this;
    }

    @Override
    public HttpCookie setMaxAge(Long age) {
        cookie.setMaxAge(age);
        return this;
    }

    @Override
    public HttpCookie setExpires(Instant expires) {
        cookie.setExpires(expires.atZone(ZoneOffset.UTC));
        return this;
    }

    @Override
    public HttpCookie setSecure(Boolean secure) {
        cookie.setSecure(secure);
        return this;
    }

    @Override
    public HttpCookie setHttpOnly(Boolean httpOnly) {
        cookie.setHttpOnly(httpOnly);
        return this;
    }

    @Override
    public HttpCookie setSameSite(SameSite sameSite) {
        cookie.setSameSite(
                switch (sameSite) {
                    case LAX -> Cookie.SameSite.Lax;
                    case STRICT -> Cookie.SameSite.Strict;
                    case NONE -> Cookie.SameSite.None;
                }
        );
        return this;
    }
}
