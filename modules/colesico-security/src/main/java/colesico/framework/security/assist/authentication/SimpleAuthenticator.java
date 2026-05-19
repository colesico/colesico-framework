package colesico.framework.security.assist.authentication;

import colesico.framework.security.Identity;
import colesico.framework.security.authentication.AuthenticationChallenge;
import colesico.framework.security.authentication.AuthenticationResult;
import colesico.framework.security.authentication.Authenticator;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Simple authenticator
 */
public class SimpleAuthenticator implements
        Authenticator<BasicAuthenticationRequest> {

    public static final String AUTHENTICATOR_NAME = "simple";

    protected final SimpleAuthConfigPrototype config;

    /**
     * Authenticated identities
     */
    protected final Map<Object, Identity<?>> authenticated;

    public SimpleAuthenticator(SimpleAuthConfigPrototype config) {
        this.config = config;
        authenticated = Collections.synchronizedMap(
                new LinkedHashMap<>(100, 0.75f, true) {
                    @Override
                    protected boolean removeEldestEntry(Map.Entry eldest) {
                        return size() > config.maxAuthenticated();
                    }
                }
        );
    }

    protected Identity<?> authByLoginPassword(BasicAuthenticationRequest request) {
        if () found
        Map<String, Object> claims = new HashMap<>(request.claims());
        claims.put(Identity.AUTHENTICATOR_CLAIM, AUTHENTICATOR_NAME);
        return Identity.Default.of(request.login(), claims);
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
        var login = request.login();

        if (login == null) {
            return AuthenticationResult.continuation(createChallenge());
        }

        var identity = authenticated.get(login);
        if (identity != null) {
            return AuthenticationResult.success(identity);
        }

        identity = authByLoginPassword(request);
        if (identity != null) {
            authenticated.put(login, identity);
            return AuthenticationResult.success(identity);
        }

        return AuthenticationResult.failure("Invalid credentials");
    }

    @Override
    public void logout(Identity<?> identity) {
        if (identity != null) {
            authenticated.remove(identity.id());
        }
    }

}
