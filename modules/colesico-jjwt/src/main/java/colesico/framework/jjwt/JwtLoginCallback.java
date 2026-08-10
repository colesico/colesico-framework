package colesico.framework.jjwt;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpCookie;
import colesico.framework.http.HttpCookieFactory;
import colesico.framework.security.Identity;
import colesico.framework.security.authentication.AuthenticationCallback;
import jakarta.inject.Provider;

import java.util.HashMap;

public class JwtLoginCallback implements AuthenticationCallback {

    protected static final String ACCESS_CONTROL_EXPOSE_HEADER = "Access-Control-Expose-Headers";
    protected static final String AUTHORIZATION_HEADER = "Authorization";
    protected static final String REFRESH_TOKEN_HEADER = "Refresh-Token";

    protected static final String ACCESS_TOKEN_COOKIE = "access_token";
    protected static final String REFRESH_TOKEN_COOKIE = "refresh_token";

    private final Provider<HttpContext> httpContext;
    private final JwtConfigPrototype config;
    private final JwtTokenUtils tokenUtils;
    protected final HttpCookieFactory cookieFactory;

    public JwtLoginCallback(Provider<HttpContext> httpContext, JwtConfigPrototype config, JwtTokenUtils tokenUtils, HttpCookieFactory cookieFactory) {
        this.httpContext = httpContext;
        this.config = config;
        this.tokenUtils = tokenUtils;
        this.cookieFactory = cookieFactory;
    }

    @Override
    public void onSuccess(Identity identity) {
        var subject = identity.id().toString();
        String accessToken = tokenUtils.generateAccessToken(subject, identity.claims());
        String refreshToken = tokenUtils.generateRefreshToken(subject, new HashMap<>());

        var httpResponse = httpContext.get().response();
        httpResponse.addHeader(ACCESS_CONTROL_EXPOSE_HEADER, AUTHORIZATION_HEADER + ',' + REFRESH_TOKEN_HEADER);
        httpResponse.addHeader(AUTHORIZATION_HEADER, "Bearer " + accessToken);
        httpResponse.addHeader(REFRESH_TOKEN_HEADER, refreshToken);

        var accessCookie = cookieFactory.create(ACCESS_TOKEN_COOKIE, accessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setSameSite(HttpCookie.SameSite.STRICT);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(config.accessTtl());

        var refreshCookie = cookieFactory.create(REFRESH_TOKEN_COOKIE, refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSameSite(HttpCookie.SameSite.STRICT);
        refreshCookie.setPath(config.refreshTokenUrl());
        refreshCookie.setMaxAge(config.refreshTtl());
    }

    @Override
    public void onLogout(Identity identity) {
        var accessCookie = cookieFactory.create(ACCESS_TOKEN_COOKIE, "");
        accessCookie.setHttpOnly(true);
        accessCookie.setSameSite(HttpCookie.SameSite.STRICT);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(0L);

        var refreshCookie = cookieFactory.create(REFRESH_TOKEN_COOKIE, "");
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSameSite(HttpCookie.SameSite.STRICT);
        refreshCookie.setPath(config.refreshTokenUrl());
        refreshCookie.setMaxAge(0L);
    }
}
