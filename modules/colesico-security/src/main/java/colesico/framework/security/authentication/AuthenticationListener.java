package colesico.framework.security.authentication;

import colesico.framework.security.Identity;

public interface AuthenticationListener {

    default void onLogin(AuthenticationResult<?> result) {

    }

    default void onLogout(Identity<?> identity) {
    }

}
