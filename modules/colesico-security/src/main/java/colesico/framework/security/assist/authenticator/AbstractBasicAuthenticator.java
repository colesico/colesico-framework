package colesico.framework.security.assist.authenticator;

import colesico.framework.security.Identity;
import colesico.framework.security.assist.authrequest.BasicAuthRequest;
import colesico.framework.security.authentication.AuthenticationChallenge;
import colesico.framework.security.authentication.AuthenticationResult;
import colesico.framework.security.authentication.Authenticator;
import colesico.framework.security.authentication.LogoutHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract basic authenticator
 */
abstract public class AbstractBasicAuthenticator implements
        Authenticator<BasicAuthRequest>,
        LogoutHandler {

    /**
     * Authenticated identities
     */
    private final Map<Object, Identity<?>> authenticated = new ConcurrentHashMap<>();

    abstract protected Identity<?> authByLoginPassword(BasicAuthRequest context);

    abstract protected AuthenticationChallenge createChallenge();

    @Override
    public boolean supports(BasicAuthRequest request) {
        return true;
    }

    @Override
    public AuthenticationResult login(BasicAuthRequest request) {
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
