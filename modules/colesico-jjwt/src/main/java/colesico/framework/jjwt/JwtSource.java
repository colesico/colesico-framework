package colesico.framework.jjwt;

import colesico.framework.assist.StringUtils;
import colesico.framework.http.HttpContext;
import colesico.framework.security.authentication.AuthenticationRequest;
import colesico.framework.security.authentication.AuthenticationSource;
import jakarta.inject.Provider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract public class JwtSource implements AuthenticationSource<JwtRequest, JwtChallenge> {

    public static final String ACCESS_TOKEN_COOKIE = "access_token";

    protected static final Pattern BEARER_PATTERN = Pattern.compile("^Bearer\\s+(.+)$", Pattern.CASE_INSENSITIVE);
    protected static final String AUTHORIZATION_HEADER = "Authorization";

    protected final JwtConfigPrototype config;

    protected final Provider<HttpContext> httpContext;

    public JwtSource(JwtConfigPrototype config, Provider<HttpContext> httpContext) {
        this.config = config;
        this.httpContext = httpContext;
    }

    @Override
    public JwtRequest request() {
        var httpRequest = this.httpContext.get().request();
        String authHeader = httpRequest.headers().get(AUTHORIZATION_HEADER);
        if (StringUtils.isBlank(authHeader)) return null;
        Matcher m = BEARER_PATTERN.matcher(authHeader.trim());
        if (m.matches()) {
            return new JwtRequest(m.group(1), AuthenticationRequest.sourceClaims(this.getClass()));
        }

        var cookie = httpRequest.cookies().get(ACCESS_TOKEN_COOKIE);
        if (cookie != null) {
            return new JwtRequest(cookie.value(), AuthenticationRequest.sourceClaims(this.getClass()));
        }

        return null;
    }

}
