package colesico.framework.security.assist.authentication;

import colesico.framework.security.authentication.AuthenticationRequest;

import java.util.Map;

/**
 * Login/Password authentication credentials
 */
public record BasicAuthenticationRequest(
        String login,
        String password,
        Map<String, Object> claims
) implements AuthenticationRequest {
    public static BasicAuthenticationRequest of(String login,
                                                String password,
                                                Map<String, Object> claims) {
        return new BasicAuthenticationRequest(login, password, claims);
    }
}
