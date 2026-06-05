package colesico.framework.security.assist.authentication.simple;

import colesico.framework.security.Identity;
import colesico.framework.security.assist.authentication.BasicAuthenticationChallenge;
import colesico.framework.security.assist.authentication.BasicAuthenticationRequest;
import colesico.framework.security.authentication.AuthenticationSource;
import jakarta.inject.Singleton;
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
@Singleton
public class SimpleAuth
        implements AuthenticationSource<BasicAuthenticationRequest, BasicAuthenticationChallenge> {

    protected static final Logger log = LoggerFactory.getLogger(SimpleAuth.class);

    protected final AtomicReference<BasicAuthenticationRequest> request = new AtomicReference<>();

    @Override
    public BasicAuthenticationRequest request() {
        return request.get();
    }

    @Override
    public void proceed(BasicAuthenticationChallenge challenge) {
        log.debug("Proceed challenge: {}", challenge);
    }

    @Override
    public void authenticate(Identity<?> identity) {
        log.debug("Identity {} is logged in", identity.id());
    }

    @Override
    public void unauthenticated(BasicAuthenticationRequest request, String error) {
        this.request.set(null);
        log.debug("Authentication request failure: {}", error);
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
        this.request.set(BasicAuthenticationRequest.of(login, password, SimpleAuth.class));
    }
}