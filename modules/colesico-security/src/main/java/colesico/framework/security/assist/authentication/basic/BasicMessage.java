package colesico.framework.security.assist.authentication.basic;

import colesico.framework.security.authentication.AuthenticationMessage;

/**
 * Login/Password authentication credentials
 */
public record BasicMessage(
        String login,
        String password
) implements AuthenticationMessage {
}
