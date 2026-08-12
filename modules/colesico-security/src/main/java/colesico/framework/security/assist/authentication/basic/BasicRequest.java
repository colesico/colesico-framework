package colesico.framework.security.assist.authentication.basic;

import colesico.framework.security.authentication.AuthenticatorRequest;
import colesico.framework.security.authentication.AuthenticationSource;

import java.util.Map;

/**
 * Login/Password authentication credentials
 */
public record BasicRequest(
        String login,
        String password
) implements AuthenticatorRequest {

    public static BasicRequest of(String login,
                                  String password,
                                  Class<? extends AuthenticationSource> sourceClass) {

        return new BasicRequest(login, password, AuthenticatorRequest.sourceClaims(sourceClass));
    }

    public static BasicRequest empty(Class<? extends AuthenticationSource> sourceClass) {
        return new BasicRequest(null, null, AuthenticatorRequest.sourceClaims(sourceClass));
    }

    public boolean isEmpty() {
        return login == null && password == null;
    }
}
