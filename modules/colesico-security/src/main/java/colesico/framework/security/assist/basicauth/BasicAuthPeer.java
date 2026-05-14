package colesico.framework.security.assist.basicauth;


import colesico.framework.security.Identity;
import colesico.framework.security.authentication.AuthenticationContext;
import colesico.framework.security.authentication.AuthenticationPeer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicAuthPeer implements AuthenticationPeer {

    private static final Logger log = LoggerFactory.getLogger(BasicAuthPeer.class);

    // current identity
    private Object identityId;

    @Override
    public AuthenticationContext context() {
        if (identityId == null) {
            return null;
        }
        return BasicAuthContext.of(identityId);
    }

    @Override
    public <C> void proceed(C challenge) {

    }

    @Override
    public void login(Identity<?> identity) {
        this.identityId = identity.id();
        log.debug("Identity {} is logged in", identity.id());
    }

    @Override
    public void logout(Identity<?> identity) {
        this.identityId = null;
        log.debug("Identity {} is logged out", identity.id());
    }
}
