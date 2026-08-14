package colesico.framework.telehttp.authentication;

import colesico.framework.assist.StringUtils;
import colesico.framework.http.HttpContext;
import colesico.framework.security.Identity;
import colesico.framework.security.assist.authentication.basic.BasicMessage;
import colesico.framework.security.assist.authentication.basic.BasicPeer;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class HttpBasicPeer implements BasicPeer {

    protected static final Pattern BASIC_AUTH_PATTERN =
            Pattern.compile("^Basic\\s+(.+)$", Pattern.CASE_INSENSITIVE);

    public static final String AUTHORIZATION_HEADER = "authorization";
    public static final String WWW_AUTHENTICATE_HEADER = "WWW-Authenticate";

    private final Provider<HttpContext> httpContext;

    public HttpBasicPeer(Provider<HttpContext> httpContext) {
        this.httpContext = httpContext;
    }

    @Override
    public BasicMessage message() {
        var request = httpContext.get().request();
        String authHeader = request.headers().get(AUTHORIZATION_HEADER);
        if (StringUtils.isBlank(authHeader)) {
            return null;
        }
        Matcher matcher = BASIC_AUTH_PATTERN.matcher(authHeader.trim());
        if (!matcher.matches()) {
            return null;
        }

        String base64Credentials = matcher.group(1);

        byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(decodedBytes, StandardCharsets.UTF_8);

        String[] values = credentials.split(":", 2);
        if (values.length == 2) {
            return new BasicMessage(values[0], values[1]);
        } else {
            throw new SecurityException("Invalid Authorization header");
        }
    }

    @Override
    public void challenge(String realm) {
        var response = httpContext.get().response();
        response
                .addHeader(WWW_AUTHENTICATE_HEADER, "Basic realm=\"" + realm + "\"")
                .setStatus(401)
                .send("401 Unauthorized. Authentication required");

    }

    @Override
    public void logout(Identity<?> identity) {
        httpContext.get().response().setStatus(401).send("Logout");
    }
}
