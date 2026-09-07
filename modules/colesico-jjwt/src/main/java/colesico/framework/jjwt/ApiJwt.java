package colesico.framework.jjwt;

import jakarta.inject.Singleton;

@Singleton
public class ApiJwt extends JwtAuthenticator {
    public ApiJwt(JwtTokenUtils tokenUtils, ApiJwtClient client) {
        super(tokenUtils, client);
    }
}
