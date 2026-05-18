package colesico.framework.security.assist.basicauth;


import colesico.framework.security.Identity;
import colesico.framework.security.assist.authrequest.BasicAuthRequest;
import colesico.framework.security.authentication.AuthenticationChallenge;
import colesico.framework.security.authentication.AuthenticationRequest;
import colesico.framework.security.authentication.AuthenticationSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

public class BasicAuthSource implements AuthenticationSource {

    private static final Logger log = LoggerFactory.getLogger(BasicAuthSource.class);

    // current identity
    private final AtomicReference<Object> identityId = new AtomicReference<>();

    @Override
    public AuthenticationRequest request() {
        if (identityId.get() == null) {
            return null;
        }
        return BasicAuthRequest.of(identityId.get());
    }

    @Override
    public <C extends AuthenticationChallenge> void proceed(C challenge) {

    }

    @Override
    public void authenticate(Identity<?> identity) {
        identityId.set(identity.id());
        log.debug("Identity {} is logged in", identity.id());
    }

    @Override
    public void logout(Identity<?> identity) {
        identityId.set(null);
        log.debug("Identity {} is logged out", identity.id());
    }
}
