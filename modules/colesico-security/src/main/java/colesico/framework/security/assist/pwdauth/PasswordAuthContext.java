package colesico.framework.security.assist.pwdauth;

import colesico.framework.security.authentication.AuthenticationContext;

/**
 * Login/Password authentication context
 *
 * @param identityId for successfully authenticated user
 */
public record PasswordAuthContext<I>(
        String username,
        String password,
        I identityId
) implements AuthenticationContext {

    public static <I> PasswordAuthContext<I> of(String username, String password) {
        return new PasswordAuthContext<>(username, password, null);
    }

    public static <I> PasswordAuthContext<I> of(I identityId) {
        return new PasswordAuthContext<>(null, null, identityId);
    }
}
