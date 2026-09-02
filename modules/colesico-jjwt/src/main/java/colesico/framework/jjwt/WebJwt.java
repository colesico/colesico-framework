package colesico.framework.jjwt;

import jakarta.inject.Singleton;

@Singleton
public class WebJwt extends Jwt{
    public WebJwt(JwtTokenUtils tokenUtils, WebJwtClient client) {
        super(tokenUtils, client);
    }
}
