package colesico.framework.security.assist.authentication;

import colesico.framework.security.Identity;
import colesico.framework.security.authentication.AuthenticationChallenge;
import colesico.framework.security.authentication.AuthenticationRequest;
import colesico.framework.security.authentication.AuthenticationSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static colesico.framework.security.authentication.AuthenticationRequest.SOURCE_CLAIM;

/**
 * Simple authentication source.
 * Allow to authenticate single user per scope  (default - singleton)
 * Put this source to appropriate scope to support multi user authentication.
 */
public class SimpleAuthenticationSource implements AuthenticationSource {

    public static final String SOURCE_NAME = "simple";

    protected static final Logger log = LoggerFactory.getLogger(SimpleAuthenticationSource.class);

    protected final AtomicReference<BasicAuthenticationRequest> request = new AtomicReference<>();

    @Override
    public BasicAuthenticationRequest request() {
        return request.get();
    }

    @Override
    public <C extends AuthenticationChallenge> void proceed(C challenge) {
        log.debug("Proceed challenge: {}", challenge);
    }

    @Override
    public void authenticate(Identity<?> identity) {
        log.debug("Identity {} is logged in", identity.id());
    }

    @Override
    public void unauthenticated(AuthenticationRequest request) {
        this.request.set(null);
        log.debug("Authentication request failure");
    }

    @Override
    public void logout(Identity<?> identity) {
        request.set(null);
        log.debug("Identity {} is logged out", identity.id());
    }

    /**
     * Credentials to perform authentication
     */
    public void setCredentials(String login, String password) {
        Map<String, Object> claims = Map.of(SOURCE_CLAIM, SOURCE_NAME);
        this.request.set(BasicAuthenticationRequest.of(login, password, claims));
    }
}