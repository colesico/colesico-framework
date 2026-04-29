package colesico.framework.security.authentication;

import colesico.framework.security.SecurityManager;

public record BasicCredentials(
        String login,
        String password
) implements SecurityManager.Credentials {
}
