package colesico.framework.security.assist.basicauth;

import colesico.framework.security.Identity;
import colesico.framework.security.authentication.AuthenticationStatus;
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

    private AuthenticationStatus authById(Object identityId) {
        var identity = identities.get(identityId);
        if (identity != null) {
            return AuthenticationStatus.success(identity);
        }
        return AuthenticationStatus.failure("Not authenticated");
    }

    private AuthenticationStatus authByLoginPassword(BasicAuthContext<Object> context) {
        return null;
    }

    @Override
    public AuthenticationStatus login(BasicAuthContext<Object> context) {
        if (context.identityId() != null) {
            return authById(context.identityId());
        } else if (context.username() != null && context.password() != null) {
            return authByLoginPassword(context);
        }
        return AuthenticationStatus.failure("Invalid credentials");
    }

    @Override
    public void logout(Identity<?> identity) {
        identities.remove(identity.id());
    }
}
