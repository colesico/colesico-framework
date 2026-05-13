package colesico.framework.security.assist.pwdauth;

import colesico.framework.security.Identity;
import colesico.framework.security.authentication.AuthenticationResult;
import colesico.framework.security.authentication.Authenticator;

public class PasswordAuthenticator implements Authenticator<PasswordAuthContext<Object>> {

    /**
     * Current identity
     */
    private Identity<?> identity;

    @Override
    public AuthenticationResult<?> login(PasswordAuthContext<Object> context) {
        if (context.identityId() != null) {
            return AuthenticationResult.success(Identity.Default.of(context.identityId()));
        }
        return null;
    }

    @Override
    public void logout(Identity<?> identity) {

    }
}
