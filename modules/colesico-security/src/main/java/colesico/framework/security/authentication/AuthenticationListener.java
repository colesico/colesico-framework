package colesico.framework.security.authentication;

import colesico.framework.security.Identity;

public interface AuthenticationListener {

    default AuthenticationStatus onLogin(AuthenticationStatus status) {
        return status;
    }

    default void onLogout(Identity<?> identity) {
    }

}
