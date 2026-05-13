package colesico.framework.security.assist.pwdauth;

import colesico.framework.security.authentication.AuthenticationContext;

/**
 * Helper context
 */
public record PasswordAuthContext(
        String username,
        String password
) implements AuthenticationContext {

    public static PasswordAuthContext of(String username, String password) {
        return new PasswordAuthContext(username, password);
    }
}
