package colesico.framework.security.assist.pwdauth;

import colesico.framework.security.Identity;
import colesico.framework.security.authentication.AuthenticationResult;
import colesico.framework.security.authentication.Authenticator;

public class PasswordAuthenticator implements Authenticator<PasswordAuthContext> {

    @Override
    public AuthenticationResult<?> login(PasswordAuthContext context) {
        return null;
    }

    @Override
    public void logout(Identity<?> identity) {

    }
}
