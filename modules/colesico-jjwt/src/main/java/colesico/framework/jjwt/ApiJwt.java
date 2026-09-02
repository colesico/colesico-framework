package colesico.framework.jjwt;

import jakarta.inject.Singleton;

@Singleton
public class ApiJwt extends Jwt{
    public ApiJwt(JwtTokenUtils tokenUtils, ApiJwtClient client) {
        super(tokenUtils, client);
    }
}
