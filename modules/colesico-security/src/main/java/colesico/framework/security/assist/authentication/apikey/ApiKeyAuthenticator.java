package colesico.framework.security.assist.authentication.apikey;

import colesico.framework.security.Identity;
import colesico.framework.security.authentication.AuthenticationChallenge;
import colesico.framework.security.authentication.AuthenticationResult;
import colesico.framework.security.authentication.Authenticator;

public class ApiKeyAuthenticator implements Authenticator<ApiKeyAuthenticationRequest, AuthenticationChallenge> {

    @Override
    public AuthenticationResult<AuthenticationChallenge> login(ApiKeyAuthenticationRequest request) {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public void logout(Identity<?> identity) {
        throw new IllegalStateException("Not implemented");
    }
}
