package colesico.framework.security.assist.authrequest;

import colesico.framework.security.authentication.AuthenticationRequest;

/**
 * Login/Password authentication credentials
 *
 * @param login for initial authentication
 * @param password for initial authentication
 */
public record BasicAuthRequest(
        String login,
        String password
) implements AuthenticationRequest {
    public static AuthenticationRequest of(String login,
                                           String password) {
        return new BasicAuthRequest(login, password);
    }
}
