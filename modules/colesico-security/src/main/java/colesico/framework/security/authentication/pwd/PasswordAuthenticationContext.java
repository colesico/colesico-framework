package colesico.framework.security.authentication.pwd;

import colesico.framework.security.authentication.AuthenticationContext;

/**
 * Helper context
 */
public record PasswordAuthenticationContext(
        String username,
        String password
) implements AuthenticationContext {

    public static PasswordAuthenticationContext of(String username, String password) {
        return new PasswordAuthenticationContext(username, password);
    }
}
