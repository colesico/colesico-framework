package colesico.framework.security.assist.authentication.basic;

import colesico.framework.security.authentication.AuthenticationMessage;
import colesico.framework.security.authentication.AuthenticationSource;

/**
 * Login/Password authentication credentials
 */
public record BasicRequest(
        String login,
        String password
) implements AuthenticationMessage {

    public static BasicRequest of(String login,
                                  String password,
                                  Class<? extends AuthenticationSource> sourceClass) {

        return new BasicRequest(login, password, AuthenticationMessage.sourceClaims(sourceClass));
    }

    public static BasicRequest empty(Class<? extends AuthenticationSource> sourceClass) {
        return new BasicRequest(null, null, AuthenticationMessage.sourceClaims(sourceClass));
    }

    public boolean isEmpty() {
        return login == null && password == null;
    }
}
