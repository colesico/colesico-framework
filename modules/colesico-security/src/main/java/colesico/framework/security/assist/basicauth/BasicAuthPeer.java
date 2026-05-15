package colesico.framework.security.assist.basicauth;


import colesico.framework.security.Identity;
import colesico.framework.security.authentication.AuthenticationChallenge;
import colesico.framework.security.authentication.AuthenticationRequest;
import colesico.framework.security.authentication.AuthenticationPeer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

public class BasicAuthPeer implements AuthenticationPeer {

    private static final Logger log = LoggerFactory.getLogger(BasicAuthPeer.class);

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
