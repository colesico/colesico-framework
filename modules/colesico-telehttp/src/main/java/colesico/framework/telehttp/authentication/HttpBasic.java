package colesico.framework.telehttp.authentication;

import colesico.framework.assist.StringUtils;
import colesico.framework.http.HttpContext;
import colesico.framework.security.Identity;
import colesico.framework.security.assist.authentication.basic.BasicAuthenticationChallenge;
import colesico.framework.security.assist.authentication.basic.BasicAuthenticationRequest;
import colesico.framework.security.authentication.AuthenticationSource;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class HttpBasic implements AuthenticationSource<BasicAuthenticationRequest, BasicAuthenticationChallenge> {

    protected static final Pattern BASIC_AUTH_PATTERN =
            Pattern.compile("^Basic\\s+(.+)$", Pattern.CASE_INSENSITIVE);

    public static final String AUTHORIZATION_HEADER = "authorization";
    public static final String WWW_AUTHENTICATE_HEADER = "WWW-Authenticate";

    private final Provider<HttpContext> httpContext;

    public HttpBasic(Provider<HttpContext> httpContext) {
        this.httpContext = httpContext;
    }

    @Override
    public BasicAuthenticationRequest request() {
        var request = httpContext.get().request();
        String authHeader = request.headers().get(AUTHORIZATION_HEADER);
        if (StringUtils.isBlank(authHeader)) {
            return BasicAuthenticationRequest.empty(HttpBasic.class);
        }
        Matcher matcher = BASIC_AUTH_PATTERN.matcher(authHeader.trim());
        if (!matcher.matches()) {
            return BasicAuthenticationRequest.empty(HttpBasic.class);
        }

        String base64Credentials = matcher.group(1);

        byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(decodedBytes, StandardCharsets.UTF_8);

        String[] values = credentials.split(":", 2);
        if (values.length == 2) {
            return BasicAuthenticationRequest.of(values[0], values[1], HttpBasic.class);
        } else {
            throw new SecurityException("Invalid Authorization header");
        }
    }

    @Override
    public void onStage(BasicAuthenticationChallenge challenge) {
        var response = httpContext.get().response();
        response
                .addHeader(WWW_AUTHENTICATE_HEADER, "Basic realm=\"" + challenge.realm() + "\"")
                .setStatus(401)
                .send("401 Unauthorized. Authentication required");

    }

    @Override
    public void onSuccess(Identity<?> identity) {

    }

    @Override
    public <E> void onFailure(BasicAuthenticationRequest request, E error) {
        httpContext.get().response()
                .setStatus(401)
                .send("401 Unauthorized. Authentication required");
    }

    @Override
    public void onLogout(Identity<?> identity) {
        httpContext.get().response().setStatus(401).send("Logout");
    }
}
