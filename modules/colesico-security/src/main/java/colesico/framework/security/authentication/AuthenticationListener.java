package colesico.framework.security.authentication;

import colesico.framework.security.Identity;

public interface AuthenticationListener {

    default AuthenticationResult onLogin(AuthenticationResult status) {
        return status;
    }

    default void onLogout(Identity<?> identity) {
    }

}
