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
public class HttpJwtClient extends JwtClient {

    protected static final String ACCESS_CONTROL_EXPOSE_HEADER = "Access-Control-Expose-Headers";
    protected static final String AUTHORIZATION_HEADER = "Authorization";
    protected static final String REFRESH_TOKEN_HEADER = "Refresh-Token";

    protected static final String ACCESS_TOKEN_COOKIE = "access_token";
    protected static final String REFRESH_TOKEN_COOKIE = "refresh_token";

    protected static final Pattern BEARER_PATTERN = Pattern.compile("^Bearer\\s+(.+)$", Pattern.CASE_INSENSITIVE);

    protected static final String TOKEN_EXPIRED_ERROR = "token_expired";

    protected final Provider<HttpContext> httpContext;
    protected final HttpCookieFactory cookieFactory;
    private final JwtConfigPrototype config;

    public HttpJwtClient(Provider<HttpContext> httpContext, HttpCookieFactory cookieFactory, JwtConfigPrototype config) {
        this.httpContext = httpContext;
        this.cookieFactory = cookieFactory;
        this.config = config;
    }

    @Override
    public void populateTokens(String accessToken, String refreshToken) {
        var response = httpContext.get().response();

        response
                .addHeader(ACCESS_CONTROL_EXPOSE_HEADER, AUTHORIZATION_HEADER + ',' + REFRESH_TOKEN_HEADER)
                .addHeader(AUTHORIZATION_HEADER, "Bearer " + accessToken)
                .addHeader(REFRESH_TOKEN_HEADER, refreshToken);

        var accessCookie = cookieFactory.create(ACCESS_TOKEN_COOKIE, accessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setSameSite(HttpCookie.SameSite.STRICT);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(config.accessTtl());
        response.addCookie(accessCookie);

        var refreshCookie = cookieFactory.create(REFRESH_TOKEN_COOKIE, refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSameSite(HttpCookie.SameSite.STRICT);
        refreshCookie.setPath(config.refreshTokenUrl());
        refreshCookie.setMaxAge(config.refreshTtl());
        response.addCookie(refreshCookie);
    }

    @Override
    public String readAccessToken() {
        var request = httpContext.get().request();

        String token = request.headers().get(AUTHORIZATION_HEADER);
        if (StringUtils.isBlank(token)) {
            var tokenCookie = request.cookies().get(ACCESS_TOKEN_COOKIE);
            if (tokenCookie == null) {
                return null;
            }
            token = tokenCookie.value();
            if (StringUtils.isBlank(token)) {
                return null;
            }
        }

        Matcher m = BEARER_PATTERN.matcher(token.trim());
        if (m.matches()) {
            return m.group(1);
        }

        return null;
    }

    @Override
    public String readRefreshToken() {
        var request = httpContext.get().request();

        var token = request.headers().get(REFRESH_TOKEN_HEADER);
        if (StringUtils.isBlank(token)) {
            var tokenCookie = request.cookies().get(REFRESH_TOKEN_COOKIE);
            if (tokenCookie == null) {
                return null;
            }
            token = tokenCookie.value();
            if (StringUtils.isBlank(token)) {
                return null;
            }
        }
        return token;
    }

    @Override
    public void askRefreshToken() {

    }

    @Override
    public void clearTokens() {
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