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
public class HttpJwtClient implements JwtClient {

    protected static final String ACCESS_CONTROL_EXPOSE_HEADER = "Access-Control-Expose-Headers";
    protected static final String AUTHORIZATION_HEADER = "Authorization";
    protected static final String REFRESH_TOKEN_HEADER = "Refresh-Token";

    protected static final String ACCESS_TOKEN_COOKIE = "access_token";
    protected static final String REFRESH_TOKEN_COOKIE = "refresh_token";

    protected static final Pattern BEARER_PATTERN = Pattern.compile("^Bearer\\s+(.+)$", Pattern.CASE_INSENSITIVE);

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

        // Allows frontend applications (e.g., Next.js/React) to read these headers during CORS requests
        response
                .addHeader(ACCESS_CONTROL_EXPOSE_HEADER, AUTHORIZATION_HEADER + ',' + REFRESH_TOKEN_HEADER)
                .addHeader(AUTHORIZATION_HEADER, "Bearer " + accessToken)
                .addHeader(REFRESH_TOKEN_HEADER, refreshToken);

        var accessCookie = cookieFactory.create(ACCESS_TOKEN_COOKIE, accessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setSameSite(HttpCookie.SameSite.STRICT);
        accessCookie.setSecure(true); // Protection against network interception (MITM)
        accessCookie.setPath("/");
        accessCookie.setMaxAge(config.accessTtl());
        response.addCookie(accessCookie);

        var refreshCookie = cookieFactory.create(REFRESH_TOKEN_COOKIE, refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSameSite(HttpCookie.SameSite.STRICT);
        refreshCookie.setSecure(true); // Protection against network interception (MITM)
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(config.refreshTtl());
        response.addCookie(refreshCookie);
    }

    @Override
    public String readAccessToken() {
        var request = httpContext.get().request();

        // Try to read from the Authorization header (checking for the Bearer prefix)
        String authHeader = request.headers().get(AUTHORIZATION_HEADER);
        if (!StringUtils.isBlank(authHeader)) {
            Matcher m = BEARER_PATTERN.matcher(authHeader.trim());
            if (m.matches()) {
                return m.group(1);
            }
        }

        // If the header is empty, retrieve the raw token directly from the Cookie
        var tokenCookie = request.cookies().get(ACCESS_TOKEN_COOKIE);
        if (tokenCookie != null && !StringUtils.isBlank(tokenCookie.value())) {
            return tokenCookie.value(); // Returned as is, without Bearer verification
        }

        return null;
    }

    @Override
    public String readRefreshToken() {
        var request = httpContext.get().request();

        String token = request.headers().get(REFRESH_TOKEN_HEADER);
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
        var response = httpContext.get().response();
        response
                .setStatus(401)
        .setContentType("application/json")
                .send("{\"error\":\"REFRESH_TOKEN_REQUIRED\"}");

    }

    @Override
    public void clearTokens() {
        var response = httpContext.get().response(); // Retrieve the current response context

        var accessCookie = cookieFactory.create(ACCESS_TOKEN_COOKIE, "");
        accessCookie.setHttpOnly(true);
        accessCookie.setSameSite(HttpCookie.SameSite.STRICT);
        accessCookie.setSecure(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(0L);
        response.addCookie(accessCookie); // Fixed: successfully appended to the response to ensure deletion

        var refreshCookie = cookieFactory.create(REFRESH_TOKEN_COOKIE, "");
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSameSite(HttpCookie.SameSite.STRICT);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(0L);
        response.addCookie(refreshCookie); // Fixed: successfully appended to the response to ensure deletion
    }
}