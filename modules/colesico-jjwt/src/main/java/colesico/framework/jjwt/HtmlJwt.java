package colesico.framework.jjwt;

import colesico.framework.http.HttpContext;
import colesico.framework.security.authentication.AuthenticationRequest;
import jakarta.inject.Provider;

public class HtmlJwt extends JwtSource {

    public static final String ACCESS_TOKEN_COOKIE = "access_token";

    public HtmlJwt(JwtConfigPrototype config, Provider<HttpContext> httpContext) {
        super(config, httpContext);
    }

    @Override
    public JwtRequest request() {
        var cookie = httpContext.get().request().cookies().get(ACCESS_TOKEN_COOKIE);
        if (cookie != null) {
            // return new JwtRequest(cookie.value(), AuthenticationRequest.sourceClaims(this.getClass()));
            return null;
        }
        return null;
    }


}
