package colesico.framework.jjwt;

import colesico.framework.assist.StringUtils;
import colesico.framework.http.HttpContext;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class RestJwtClient extends JwtClient {

    protected static final Pattern BEARER_PATTERN = Pattern.compile("^Bearer\\s+(.+)$", Pattern.CASE_INSENSITIVE);
    protected static final String AUTHORIZATION_HEADER = "Authorization";
    protected static final String REFRESH_TOKEN_HEADER = "refresh-token";


    public RestJwtClient(Provider<HttpContext> httpContext) {
        super(httpContext);
    }

    @Override
    public void populateTokens(String accessToken, String refreshToken) {
        // Implement token deployment to response headers/cookies if required by the framework
    }

    @Override
    String retrieveAccessToken() {
        String authHeader = this.httpContext.get().request().headers().get(AUTHORIZATION_HEADER);
        if (StringUtils.isBlank(authHeader)) return null;

        Matcher m = BEARER_PATTERN.matcher(authHeader.trim());
        if (m.matches()) {
            return m.group(1);
        }

        return null;
    }

    @Override
    String retrieveRefreshToken() {
        var request = httpContext.get().request();
        return request.headers().get(REFRESH_TOKEN_HEADER);
    }
}