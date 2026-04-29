package colesico.framework.security.internal;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.key.ClassedKey;
import colesico.framework.security.Principal;
import colesico.framework.security.SecurityManager;
import colesico.framework.security.authentication.AuthenticationManager;
import colesico.framework.security.authentication.AuthenticationProvider;

public class AuthenticationManagerImpl implements AuthenticationManager<?> {

    protected final Ioc ioc;

    public AuthenticationManagerImpl(Ioc ioc) {
        this.ioc = ioc;
    }

    @Override
    public Principal<?> authenticate(SecurityManager.Credentials credentials) {
        AuthenticationProvider<P, SecurityManager.Credentials> authProvider = ioc.instance(new ClassedKey<>(AuthenticationProvider.class, credentials.getClass()));
        var principleOpt = authProvider.authenticate(credentials);
        if (principleOpt.isPresent()) {
            write(principleOpt.get());
            return principleOpt.get();
        }
        return null;
    }

}
