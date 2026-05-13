package colesico.framework.security.authentication.pwd;

import colesico.framework.security.Identity;
import colesico.framework.security.authentication.AuthenticationResult;
import colesico.framework.security.authentication.Authenticator;

public class PasswordAuthenticator implements Authenticator<PasswordAuthenticationContext> {

    @Override
    public AuthenticationResult<?> authenticate(PasswordAuthenticationContext context) {
        return null;
    }

    @Override
    public void logout(Identity<?> identity) {

    }
}
