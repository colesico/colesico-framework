package colesico.framework.security.assist.basicauth;

import colesico.framework.security.Identity;
import colesico.framework.security.authentication.AuthenticationResult;
import colesico.framework.security.authentication.Authenticator;
import colesico.framework.security.authentication.LogoutHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default basic authenticator (in-memory)
 */
abstract public class BasicAuthenticator implements
        Authenticator<BasicAuthRequest<Object>>,
        LogoutHandler {

    /**
     * Authenticated identities
     */
    private final Map<Object, Identity<?>> identities = new ConcurrentHashMap<>();

    protected AuthenticationResult authById(Object identityId) {
        var identity = identities.get(identityId);
        if (identity != null) {
            return AuthenticationResult.success(identity);
        }
        return AuthenticationResult.failure("Not authenticated");
    }

    abstract protected AuthenticationResult authByLoginPassword(BasicAuthRequest<Object> context);

    @Override
    public boolean supports(BasicAuthRequest<Object> request) {
        return true;
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
