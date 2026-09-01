package colesico.framework.jjwt;

import colesico.framework.http.HttpContext;
import jakarta.inject.Provider;

abstract public class JwtClient {

    protected final Provider<HttpContext> httpContext;

    public JwtClient(Provider<HttpContext> httpContext) {
        this.httpContext = httpContext;
    }

    abstract public void populateTokens(String accessToken, String refreshToken);

    abstract String retrieveAccessToken();

    abstract String retrieveRefreshToken();

}
