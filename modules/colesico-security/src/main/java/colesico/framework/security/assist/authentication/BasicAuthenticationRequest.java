package colesico.framework.security.assist.authentication;

import colesico.framework.security.authentication.AuthenticationRequest;

/**
 * Login/Password authentication credentials
 *
 * @param login for initial authentication
 * @param password for initial authentication
 */
public record BasicAuthenticationRequest(
        String login,
        String password
) implements AuthenticationRequest {
    public static AuthenticationRequest of(String login,
                                           String password) {
        return new BasicAuthenticationRequest(login, password);
    }
}
