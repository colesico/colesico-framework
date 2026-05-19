package colesico.framework.security.authentication;

import colesico.framework.ioc.production.Polyproduce;
import colesico.framework.security.Identity;

import java.util.Optional;

/**
 * Authentication handler/listener
 * To register handler use {@link Polyproduce}
 */
public interface AuthenticationHandler {

    default HandleResult handleLogin(Optional<AuthenticationRequest> request, AuthenticationResult result) {
        return new HandleResult(result,true);
    }

    default void handleLogout(Identity<?> identity) {
    }

    record HandleResult(AuthenticationResult result, boolean porceed){

    }

}
