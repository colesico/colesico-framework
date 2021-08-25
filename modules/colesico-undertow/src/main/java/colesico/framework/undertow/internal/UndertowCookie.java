package colesico.framework.undertow.internal;

import colesico.framework.http.HttpCookie;
import io.undertow.server.handlers.Cookie;

import java.util.Date;

public class UndertowCookie implements HttpCookie {

    public static final String STRICT_MODE = "strict";
    public static final String LAX_MODE = "lax";
    
    private Cookie undertowCookie;

    public UndertowCookie(Cookie undertowCookie) {
        this.undertowCookie = undertowCookie;
    }

    public Cookie getUndertowCookie() {
        return undertowCookie;
    }

    @Override
    public String getName() {
        return undertowCookie.getName();
    }

    @Override
    public String getValue() {
        return undertowCookie.getValue();
    }

    @Override
    public String getDomain() {
        return undertowCookie.getDomain();
    }

    @Override
    public String getPath() {
        return undertowCookie.getPath();
    }

    @Override
    public Integer getMaxAge() {
        return undertowCookie.getMaxAge();
    }

    @Override
    public Date getExpires() {
        return undertowCookie.getExpires();
    }

    @Override
    public Boolean getSecure() {
        return undertowCookie.isSecure();
    }

    @Override
    public SameSite getSameSite() {
        String ssm = undertowCookie.getSameSiteMode();
        if (ssm != null) {
            switch (ssm) {
                case STRICT_MODE:
                    return SameSite.STRICT;
                case LAX_MODE:
                    return SameSite.LAX;
                default:
                    throw new RuntimeException("Unsupported SameSite mode: " + ssm);
            }
        }
        return null;
    }

    @Override
    public Boolean getHttpOnly() {
        return undertowCookie.isHttpOnly();
    }

    @Override
    public HttpCookie setValue(String value) {
        undertowCookie.setValue(value);
        return this;
    }

    @Override
    public HttpCookie setDomain(String domain) {
        undertowCookie.setDomain(domain);
        return this;
    }

    @Override
    public HttpCookie setPath(String path) {
        undertowCookie.setPath(path);
        return this;
    }

    @Override
    public HttpCookie setMaxAge(Integer age) {
        undertowCookie.setMaxAge(age);
        return this;
    }

    @Override
    public HttpCookie setExpires(Date expires) {
        undertowCookie.setExpires(expires);
        return this;
    }

    @Override
    public HttpCookie setSecure(Boolean secure) {
        undertowCookie.setSecure(secure);
        return this;
    }

    @Override
    public HttpCookie setHttpOnly(Boolean httpOnly) {
        undertowCookie.setHttpOnly(httpOnly);
        return this;
    }

    @Override
    public HttpCookie setSameSite(SameSite sameSite) {
        switch (sameSite) {
            case STRICT:
                undertowCookie.setSameSiteMode(STRICT_MODE);
                break;
            case LAX:
                undertowCookie.setSameSiteMode(LAX_MODE);
                break;
        }
        return this;
    }

}
