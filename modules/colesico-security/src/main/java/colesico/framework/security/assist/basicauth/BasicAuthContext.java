package colesico.framework.security.assist.basicauth;

import colesico.framework.security.authentication.AuthenticationContext;

/**
 * Login/Password authentication credentials
 *
 * @param identityId for already authenticated user
 * @param username   for initial authentication
 * @param password   for initial authentication
 */
public record BasicAuthContext<I>(
        String username,
        String password,
        I identityId
) implements AuthenticationContext {

    public static <I> BasicAuthContext<I> of(String username, String password) {
        return new BasicAuthContext<>(username, password, null);
    }

    public static <I> BasicAuthContext<I> of(I identityId) {
        return new BasicAuthContext<>(null, null, identityId);
    }
}
