package colesico.framework.security.assist.basicauth;

import colesico.framework.security.Identity;
import colesico.framework.security.authentication.AuthenticationResult;
import colesico.framework.security.authentication.Authenticator;

import java.util.HashMap;
import java.util.Map;

/**
 * Default basic authenticator (in-memory)
 */
public class BasicAuthenticator implements Authenticator<BasicAuthRequest<Object>> {

    /**
     * Authenticated identities
     */
    private final Map<Object, Identity<?>> identities = new HashMap<>();

    private AuthenticationResult authById(Object identityId) {
        var identity = identities.get(identityId);
        if (identity != null) {
            return AuthenticationResult.success(identity);
        }
        return AuthenticationResult.failure("Not authenticated");
    }

    private AuthenticationResult authByLoginPassword(BasicAuthRequest<Object> context) {
        return null;
    }

    @Override
    public AuthenticationResult login(BasicAuthRequest<Object> request) {
        if (request.identityId() != null) {
            return authById(request.identityId());
        } else if (request.username() != null && request.password() != null) {
            return authByLoginPassword(request);
        }
        return AuthenticationResult.failure("Invalid credentials");
    }

    @Override
    public void logout(Identity<?> identity) {
        identities.remove(identity.id());
    }
}
