package colesico.framework.security;

import colesico.framework.security.authentication.AuthenticationContext;
import colesico.framework.security.authentication.AuthenticationResult;

public interface SecurityListener {

    default AuthenticationResult<?> onAuthenticate(AuthenticationResult<?> res) { return identity; }

    default void onLogout(Identity<?> identity) {}

}
