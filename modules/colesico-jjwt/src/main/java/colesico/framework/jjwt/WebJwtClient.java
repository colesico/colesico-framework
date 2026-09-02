package colesico.framework.jjwt;

import colesico.framework.assist.StringUtils;
import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpCookie;
import colesico.framework.http.HttpCookieFactory;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class WebJwtClient implements JwtClient {

    protected static final String ACCESS_TOKEN_COOKIE = "access_token";
    protected static final String REFRESH_TOKEN_COOKIE = "refresh_token";

    protected final Provider<HttpContext> httpContext;
    protected final HttpCookieFactory cookieFactory;
    protected final JwtConfigPrototype config;

    public WebJwtClient(Provider<HttpContext> httpContext, HttpCookieFactory cookieFactory, JwtConfigPrototype config) {
        this.httpContext = httpContext;
        this.cookieFactory = cookieFactory;
        this.config = config;
    }

    protected HttpCookie createCookie(String name, String value, Long ttl) {
        var cookie = cookieFactory.create(name, value);
        cookie.setHttpOnly(true);
        cookie.setSameSite(HttpCookie.SameSite.STRICT);
        cookie.setSecure(false); // Protection against network interception (MITM)
        cookie.setPath("/");
        cookie.setMaxAge(ttl);
        return cookie;
    }

    @Override
    public void populateTokens(String accessToken, String refreshToken) {
        httpContext.get().response()
                .addCookie(createCookie(ACCESS_TOKEN_COOKIE, accessToken, config.accessTtl()))
                .addCookie(createCookie(REFRESH_TOKEN_COOKIE, refreshToken, config.refreshTtl()));
    }


    @Override
    public String readAccessToken() {
        var request = httpContext.get().request();

        var tokenCookie = request.cookies().get(ACCESS_TOKEN_COOKIE);
        if (tokenCookie != null && !StringUtils.isBlank(tokenCookie.value())) {
            return tokenCookie.value().trim();
        }

        return null;
    }

    @Override
    public String readRefreshToken() {
        var request = httpContext.get().request();

        var tokenCookie = request.cookies().get(REFRESH_TOKEN_COOKIE);
        if (tokenCookie == null) {
            return null;
        }
        var token = tokenCookie.value();
        if (StringUtils.isBlank(token)) {
            return null;
        }

        return token;
    }

    @Override
    public void askRefreshToken() {
        var response = httpContext.get().response();
        response
                .setStatus(302)
                .setLocation(config.loginUrl())
                .close();
    }

    @Override
    public void clearTokens() {
        httpContext.get().response()
                .addCookie(createCookie(ACCESS_TOKEN_COOKIE, "", 0L))
                .addCookie(createCookie(REFRESH_TOKEN_COOKIE, "", 0L));
    }
}