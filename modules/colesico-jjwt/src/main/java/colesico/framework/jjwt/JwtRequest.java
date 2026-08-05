package colesico.framework.jjwt;

import colesico.framework.security.authentication.AuthenticationRequest;

import java.util.Map;

public record JwtRequest(
        String token,
        Map<String, Object> claims)
        implements AuthenticationRequest {

}
