package colesico.framework.security.assist.authentication.basic;

import colesico.framework.security.Identity;
import colesico.framework.security.authentication.AuthenticationOutcome;
import colesico.framework.security.authentication.AuthenticationSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Simple basic authentication source.
 * Allow to authenticate single user per scope  (default - singleton)
 * Put this source to appropriate scope to support multi user authentication.
 */
public class BasicAuthentication
        implements AuthenticationSource<BasicAuthenticationRequest> {

    protected static final Logger log = LoggerFactory.getLogger(BasicAuthentication.class);

    protected final AtomicReference<BasicAuthenticationRequest> request = new AtomicReference<>();

    @Override
    public BasicAuthenticationRequest request() {
        return request.get();
    }

    @Override
    public void onStage(BasicAuthenticationChallenge challenge) {
        log.debug("Proceed challenge: {}", challenge);
    }

    @Override
    public void onSuccess(AuthenticationOutcome.Success success) {
        log.debug("Identity {} is logged in", success.identity().id());
    }

    @Override
    public <E> void onFailure(AuthenticationOutcome.Failure failure) {
        this.request.set(null);
        log.debug("Authentication request failure: {}", error);
    }

    @Override
    public void onLogout(Identity identity) {
        request.set(null);
        log.debug("Identity {} is logged out", identity.id());
    }

    /**
     * Credentials to perform authentication
     */
    public void setCredentials(String login, String password) {
        this.request.set(BasicAuthenticationRequest.of(login, password, BasicAuthentication.class));
    }
}