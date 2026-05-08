package colesico.framework.security.assist;

import colesico.framework.security.authentication.AuthenticationRequest;

public record BasicAuthRequest(
        String login,
        String password
) implements AuthenticationRequest {
}
