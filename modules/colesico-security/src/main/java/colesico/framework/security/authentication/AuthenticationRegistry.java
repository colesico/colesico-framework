package colesico.framework.security.authentication;

import colesico.framework.security.Identity;

import java.util.Optional;

public interface AuthenticationRegistry {
    Optional<LogoutHandler> findLogoutHandler(Identity<?> identity);

    Authenticator<AuthenticationRequest> findAuthenticator(AuthenticationRequest request);
}
