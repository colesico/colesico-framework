package colesico.framework.security.assist.basicauth;

import colesico.framework.security.authentication.AuthenticationRequest;

/**
 * Login/Password authentication credentials
 *
 * @param identityId for already authenticated user
 * @param username   for initial authentication
 * @param password   for initial authentication
 */
public record BasicAuthRequest<I>(
        String username,
        String password,
        I identityId
) implements AuthenticationRequest {

    public static <I> BasicAuthRequest<I> of(String username, String password) {
        return new BasicAuthRequest<>(username, password, null);
    }

    public static <I> BasicAuthRequest<I> of(I identityId) {
        return new BasicAuthRequest<>(null, null, identityId);
    }
}
