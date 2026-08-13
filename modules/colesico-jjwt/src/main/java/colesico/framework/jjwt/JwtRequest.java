package colesico.framework.jjwt;

import colesico.framework.security.authentication.AuthenticationMessage;

import java.util.Map;

public record JwtRequest(
        String accessToken,
        String refreshToken,
        Map<String, Object> claims) implements AuthenticationMessage {
}
