package colesico.framework.jjwt;

import colesico.framework.http.HttpContext;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

@Singleton
public class RestJwt extends JwtSource {

    public RestJwt(JwtConfigPrototype config, Provider<HttpContext> httpContext) {
        super(config, httpContext);
    }

    @Override
    public void proceed(JwtChallenge challenge) {
        httpContext.get().response()
                .setStatus(401)
                .send(config.accessTokenExpiredResponse());
    }

}
