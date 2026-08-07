package colesico.framework.jjwt;

import colesico.framework.security.authentication.AuthenticationRequest;

import java.util.Map;

public record JwtSubjectRequest(
        String subject,
        Map<String, Object> claims
) implements AuthenticationRequest {

}
