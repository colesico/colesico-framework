package colesico.framework.security.authentication;

import colesico.framework.security.Identity;

import java.util.Optional;

public interface AuthenticationRegistry {

    /**
     * Retrieve logout handler fot given identity based on
     * {@link Identity#LOGOUT_HANDLER_CLAIM} or default logout handler
     * for LogoutHandler interface
     */
    Optional<LogoutHandler> findLogoutHandler(Identity<?> identity);

    Authenticator<AuthenticationRequest> findAuthenticator(AuthenticationRequest request);

}
