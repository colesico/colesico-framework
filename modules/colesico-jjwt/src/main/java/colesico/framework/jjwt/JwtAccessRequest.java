package colesico.framework.jjwt;

import colesico.framework.security.authentication.AuthenticationRequest;

import java.util.Map;

public record JwtAccessRequest(
        String accessToken,
        Map<String, Object> claims) implements AuthenticationRequest {
}
