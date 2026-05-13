package colesico.framework.security.assist.basicauth;

import colesico.framework.security.Identity;
import colesico.framework.security.authentication.AuthenticationResult;
import colesico.framework.security.authentication.Authenticator;

import java.util.HashMap;
import java.util.Map;

/**
 * Default basic authenticator (in-memory)
 */
public class BasicAuthenticator implements Authenticator<BasicAuthContext<Object>> {

    /**
     * Authenticated identities
     */
    private final Map<Object, Identity<?>> identities = new HashMap<>();

    private AuthenticationResult<?> authById(Object identityId) {
        var identity = identities.get(identityId);
        if (identity != null) {
            return AuthenticationResult.success(identity);
        }
        return AuthenticationResult.failure("Not authenticated");
    }

    private AuthenticationResult<?> authByLoginPassword(BasicAuthContext<Object> context) {
        return null;
    }

    @Override
    public AuthenticationResult<?> login(BasicAuthContext<Object> context) {
        if (context.identityId() != null) {
            return authById(context.identityId());
        } else if (context.username() != null && context.password() != null) {
            return authByLoginPassword(context);
        }
        return AuthenticationResult.failure("Invalid credentials");
    }

    @Override
    public void logout(Identity<?> identity) {
        identities.remove(identity.id());
    }
}
