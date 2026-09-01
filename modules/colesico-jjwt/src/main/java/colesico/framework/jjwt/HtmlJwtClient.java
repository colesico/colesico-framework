package colesico.framework.jjwt;

import colesico.framework.http.HttpContext;
import jakarta.inject.Provider;

public class HtmlJwtClient extends JwtClient {

    public static final String ACCESS_TOKEN_COOKIE = "access_token";

    public HtmlJwtClient(JwtConfigPrototype config, Provider<HttpContext> httpContext) {
        super(config, httpContext);
    }

    @Override
    public JwtTokens getTokens() {
        var cookie = httpContext.get().request().cookies().get(ACCESS_TOKEN_COOKIE);
        if (cookie != null) {
            // return new JwtRequest(cookie.value(), AuthenticationRequest.sourceClaims(this.getClass()));
            return null;
        }
        return null;
    }


}
