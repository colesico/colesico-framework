package colesico.framework.security.authentication;

import colesico.framework.ioc.production.Polyproduce;
import colesico.framework.security.Identity;

import java.util.Optional;

/**
 * Authentication handler
 * To register handler use {@link Polyproduce}
 */
public interface AuthenticationHandler {

    default AuthenticationResult handleLogin(Optional<AuthenticationRequest> request, AuthenticationResult result) {
        return result;
    }

    default void handleLogout(Identity<?> identity) {
    }

}
