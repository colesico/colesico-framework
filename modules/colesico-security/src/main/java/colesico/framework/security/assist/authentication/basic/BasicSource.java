package colesico.framework.security.assist.authentication.basic;

import colesico.framework.security.Identity;
import colesico.framework.security.authentication.AuthenticationSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Simple basic authentication source.
 * Allow to authenticate single user per scope  (default - singleton)
 * Put this source to appropriate scope to support multi user authentication.
 */
public class BasicSource
        implements AuthenticationSource<BasicRequest, BasicCallback>, BasicCallback {

    protected static final Logger log = LoggerFactory.getLogger(BasicSource.class);

    protected final AtomicReference<BasicRequest> request = new AtomicReference<>();

    @Override
    public BasicRequest request() {
        return request.get();
    }

    @Override
    public BasicCallback callback() {
        return this;
    }

    @Override
    public void onChallenge(String realm) {
        log.debug("Proceed realm: {}", realm);
    }

    @Override
    public void onLogin(Identity identity) {
        log.debug("Identity {} is logged in", identity.id());
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
        this.request.set(BasicRequest.of(login, password, BasicSource.class));
    }
}