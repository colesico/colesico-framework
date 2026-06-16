package colesico.framework.security.assist.authentication.basic;

import colesico.framework.security.authentication.AuthenticationRequest;
import colesico.framework.security.authentication.AuthenticationSource;

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

    public static BasicAuthenticationRequest of(String login,
                                                String password,
                                                Class<? extends AuthenticationSource> sourceClass) {

        return new BasicAuthenticationRequest(login, password, AuthenticationRequest.sourceClaims(sourceClass));
    }

    public static BasicAuthenticationRequest empty(Class<? extends AuthenticationSource> sourceClass) {
        return new BasicAuthenticationRequest(null, null, AuthenticationRequest.sourceClaims(sourceClass));
    }

    public boolean isEmpty() {
        return login == null && password == null;
    }
}
