package colesico.framework.security.assist.authentication;


import colesico.framework.security.Identity;
import colesico.framework.security.authentication.AuthenticationChallenge;
import colesico.framework.security.authentication.AuthenticationRequest;
import colesico.framework.security.authentication.AuthenticationSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleAuthenticationSource<R extends AuthenticationRequest> implements AuthenticationSource {

    protected static final Logger log = LoggerFactory.getLogger(SimpleAuthenticationSource.class);

    protected R request;

    @Override
    public R request() {
        return request;
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
    public void logout(Identity<?> identity) {
        log.debug("Identity {} is logged out", identity.id());
    }

    public R getRequest() {
        return request;
    }

    public void setRequest(R request) {
        this.request = request;
    }
}
