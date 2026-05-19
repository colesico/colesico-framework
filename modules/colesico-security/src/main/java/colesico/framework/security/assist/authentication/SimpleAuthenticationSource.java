package colesico.framework.security.assist.authentication;


import colesico.framework.security.Identity;
import colesico.framework.security.authentication.AuthenticationChallenge;
import colesico.framework.security.authentication.AuthenticationRequest;
import colesico.framework.security.authentication.AuthenticationSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Put this source to appropriate scope  (i.e. session)
 */
public class SimpleAuthenticationSource<R extends AuthenticationRequest> implements AuthenticationSource {

    protected static final Logger log = LoggerFactory.getLogger(SimpleAuthenticationSource.class);

    protected final AtomicReference<R> request = new AtomicReference<>();

    @Override
    public R request() {
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

    public void setRequest(R request) {
        this.request.set(request);
    }
}
