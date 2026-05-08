package colesico.framework.security.assist;

import colesico.framework.security.authentication.AuthenticationContext;

public record BasicAuthContext(
        String login,
        String password
) implements AuthenticationContext {
}
