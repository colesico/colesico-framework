package colesico.framework.jjwt;

import colesico.framework.security.authentication.AuthenticationRequest;

import java.util.Map;

public record JwtRefreshRequest(
        String refreshToken,
        Map<String, Object> claims) implements AuthenticationRequest {
}
