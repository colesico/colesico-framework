package colesico.framework.security.authentication;

/**
 * Helper auth context
 */
public record PasswordAuthenticationContext(
        String username,
        String password
) implements AuthenticationContext {

    public static PasswordAuthenticationContext of(String username, String password) {
        return new PasswordAuthenticationContext(username, password);
    }
}
