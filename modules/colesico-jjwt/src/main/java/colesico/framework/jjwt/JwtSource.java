package colesico.framework.jjwt;

import colesico.framework.http.HttpContext;
import colesico.framework.security.authentication.AuthenticationSource;
import jakarta.inject.Provider;

abstract public class JwtSource implements AuthenticationSource<JwtRequest, JwtChallenge> {

    protected final JwtConfigPrototype config;

    protected final Provider<HttpContext> httpContext;

    public JwtSource(JwtConfigPrototype config, Provider<HttpContext> httpContext) {
        this.config = config;
        this.httpContext = httpContext;
    }

}
