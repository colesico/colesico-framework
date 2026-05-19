package colesico.framework.security.authentication;

import colesico.framework.ioc.production.Polyproduce;
import colesico.framework.security.Identity;

import java.util.Optional;

/**
 * Authentication handler/listener
 * To register handler use {@link Polyproduce}
 */
public interface AuthenticationHandler {

    default HandleResult<AuthenticationResult> handleLogin(Optional<AuthenticationRequest> request, AuthenticationResult result) {
        return new HandleResult<>(result, true);
    }

    default HandleResult<Object> handleLogout(Optional<Identity<?>> identity) {
        return new HandleResult<>(null, true);
    }

    record HandleResult<R>(R result, boolean porceed) {

    }

}
