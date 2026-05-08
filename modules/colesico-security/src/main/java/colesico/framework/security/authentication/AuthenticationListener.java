package colesico.framework.security.authentication;

import colesico.framework.security.Identity;

public interface AuthenticationListener {

    default AuthenticationResult<?> onAuthenticate(AuthenticationResult<?> result) {
        return result;
    }

    default void onLogout(Identity<?> identity) {
    }

}
