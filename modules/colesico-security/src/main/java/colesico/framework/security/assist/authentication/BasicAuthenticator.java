package colesico.framework.security.assist.authentication;

import colesico.framework.security.Identity;
import colesico.framework.security.authentication.AuthenticationChallenge;
import colesico.framework.security.authentication.AuthenticationResult;
import colesico.framework.security.authentication.Authenticator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract basic authenticator
 */
public class BasicAuthenticator implements
        Authenticator<BasicAuthenticationRequest> {

    /**
     * Authenticated identities
     */
    protected final Map<Object, Identity<?>> authenticated = new ConcurrentHashMap<>();

    protected Identity<?> authByLoginPassword(BasicAuthenticationRequest context) {
        Map<String, Object> claims = Map.of(Identity.AUTHENTICATOR_CLAIM, BasicAuthenticator.class.getCanonicalName());
        return new Identity.Default<>("0", claims);
    }

    protected AuthenticationChallenge createChallenge() {
        return null;
    }

    @Override
    public boolean supports(BasicAuthenticationRequest request) {
        return true;
    }

    @Override
    public AuthenticationResult login(BasicAuthenticationRequest request) {
        if (request.login() != null) {
            var identity = authenticated.get(request.login());
            if (identity != null) {
                return AuthenticationResult.success(identity);
            } else {
                identity = authByLoginPassword(request);
                if (identity != null) {
                    authenticated.put(identity.id(), identity);
                    return AuthenticationResult.success(identity);
                } else {
                    return AuthenticationResult.failure("Invalid credentials");
                }
            }
        }
        return AuthenticationResult.continuation(createChallenge());
    }

    @Override
    public void logout(Identity<?> identity) {
        authenticated.remove(identity.id());
    }
}
