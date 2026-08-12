package colesico.framework.security.assist.authentication.apikey;

import colesico.framework.security.Identity;
import colesico.framework.security.authentication.AuthenticationOutcome;
import colesico.framework.security.authentication.Authenticator;

public class ApiKeyAuthenticator implements Authenticator<ApiKeyAuthenticationRequest> {

    @Override
    public AuthenticationOutcome authenticate(ApiKeyAuthenticationRequest request) {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public void logout(Identity identity) {
        throw new IllegalStateException("Not implemented");
    }
}
