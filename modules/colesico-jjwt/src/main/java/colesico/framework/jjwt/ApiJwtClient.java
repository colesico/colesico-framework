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
public class ApiJwtClient implements JwtClient {

    protected static final String ACCESS_CONTROL_EXPOSE_HEADER = "Access-Control-Expose-Headers";
    protected static final String AUTHORIZATION_HEADER = "Authorization";
    protected static final String REFRESH_TOKEN_HEADER = "Refresh-Token";

    protected static final Pattern BEARER_PATTERN = Pattern.compile("^Bearer\\s+(.+)$", Pattern.CASE_INSENSITIVE);

    protected final Provider<HttpContext> httpContext;

    public ApiJwtClient(Provider<HttpContext> httpContext) {
        this.httpContext = httpContext;
    }

    @Override
    public void populateTokens(String accessToken, String refreshToken) {
        var response = httpContext.get().response();

        response
                .setHeader(ACCESS_CONTROL_EXPOSE_HEADER, AUTHORIZATION_HEADER + ',' + REFRESH_TOKEN_HEADER)
                .addHeader(AUTHORIZATION_HEADER, "Bearer " + accessToken)
                .addHeader(REFRESH_TOKEN_HEADER, refreshToken);

    }

    @Override
    public String readAccessToken() {
        // Try to read from the Authorization header (checking for the Bearer prefix)
        String authHeader = httpContext.get().request().headers().get(AUTHORIZATION_HEADER);
        if (!StringUtils.isBlank(authHeader)) {
            Matcher m = BEARER_PATTERN.matcher(authHeader.trim());
            if (m.matches()) {
                return m.group(1);
            }
        }

        return null;
    }

    @Override
    public String readRefreshToken() {
        var request = httpContext.get().request();

        String token = request.headers().get(REFRESH_TOKEN_HEADER);
        if (StringUtils.isBlank(token)) {
            return null;
        }
        return token.trim();
    }

    @Override
    public void askRefreshToken() {
        var response = httpContext.get().response();
        response
                .setStatus(401)
                .setContentType("text/plain")
                .send("RefreshTokenRequired");
    }

    @Override
    public void clearTokens() {
        var response = httpContext.get().response();
        response
                .setStatus(401)
                .setContentType("text/plain")
                .send("ClearTokens");
    }
}