package colesico.framework.security.assist.pwdauth;


import colesico.framework.security.Identity;
import colesico.framework.security.authentication.AuthenticationContext;
import colesico.framework.security.authentication.AuthenticationExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PasswordAuthExchange implements AuthenticationExchange {

    private static final Logger log = LoggerFactory.getLogger(PasswordAuthExchange.class);

    // current identity
    private Identity<?> identity;

    @Override
    public AuthenticationContext context() {
        if (identity == null){
            return null;
        }
        return null;
    }

    @Override
    public void login(Identity<?> identity) {
        this.identity = identity;
        log.debug("Identity {} is logged in", identity.id());
    }

    @Override
    public void logout(Identity<?> identity) {
        this.identity = null;
        log.debug("Identity {} is logged out", identity.id());
    }
}
