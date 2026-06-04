package colesico.framework.security.assist.authentication;

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
                                                Class<? extends AuthenticationSource> source) {
        Map<String, Object> claims = Map.of(AuthenticationRequest.SOURCE_CLAIM, source);
        return new BasicAuthenticationRequest(login, password, claims);
    }

    public static BasicAuthenticationRequest empty(Class<? extends AuthenticationSource> source) {
        Map<String, Object> claims = Map.of(AuthenticationRequest.SOURCE_CLAIM, source);
        return new BasicAuthenticationRequest(null, null, claims);
    }
}
