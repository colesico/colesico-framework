package colesico.framework.security.authentication;

import colesico.framework.security.Identity;

public interface AuthenticationListener {

    default AuthenticationResult onLogin(AuthenticationResult result, AuthenticationRequest request) {
        return result;
    }

    default void onLogout(Identity<?> identity) {
    }

}
