package colesico.framework.security.internal;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.key.ClassedKey;
import colesico.framework.ioc.key.Key;
import colesico.framework.security.Identity;
import colesico.framework.security.authentication.AuthenticationContext;
import colesico.framework.security.authentication.AuthenticationManager;
import colesico.framework.security.authentication.Authenticator;
import colesico.framework.security.authentication.AuthenticationResult;

public class AuthenticationManagerImpl implements AuthenticationManager {

    protected final Ioc ioc;

    public AuthenticationManagerImpl(Ioc ioc) {
        this.ioc = ioc;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Authenticator<AuthenticationContext> authenticator(Class<? extends AuthenticationContext> contextClass) {
        if (contextClass == null) {
            throw new SecurityException("Authentication context class is null");
        }
        Key<Authenticator> authIocKey = new ClassedKey<>(Authenticator.class, contextClass);
        Authenticator<AuthenticationContext> authenticator = ioc.instanceOrNull(authIocKey);
        if (authenticator == null) {
            throw new SecurityException("Authenticator not found for context class '" + contextClass.getCanonicalName() + "'");
        }
        return authenticator;
    }

    @Override
    public AuthenticationResult<?> authenticate(AuthenticationContext context) {
        return authenticator(context.getClass()).authenticate(context);
    }

    @Override
    public void logout(Identity<?> identity) {
        @SuppressWarnings("unchecked")
        Class<? extends AuthenticationContext> contextClass =
                (Class<? extends AuthenticationContext>) identity.claims().get(Identity.AUTHENTICATOR_CLAIM);

        if (contextClass != null) {
            authenticator(contextClass).logout(identity);
        }
    }
}
