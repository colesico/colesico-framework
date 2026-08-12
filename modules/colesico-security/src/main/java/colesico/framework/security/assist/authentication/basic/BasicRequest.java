package colesico.framework.security.assist.authentication.basic;

import colesico.framework.security.authentication.AuthenticationRequest;
import colesico.framework.security.authentication.AuthenticationSource;

import java.util.Map;

/**
 * Login/Password authentication credentials
 */
public record BasicRequest(
        String login,
        String password,
        Map<String, Object> claims
) implements AuthenticationRequest {

    public static BasicRequest of(String login,
                                  String password,
                                  Class<? extends AuthenticationSource> sourceClass) {

        return new BasicRequest(login, password, AuthenticationRequest.sourceClaims(sourceClass));
    }

    public static BasicRequest empty(Class<? extends AuthenticationSource> sourceClass) {
        return new BasicRequest(null, null, AuthenticationRequest.sourceClaims(sourceClass));
    }

    public boolean isEmpty() {
        return login == null && password == null;
    }
}
