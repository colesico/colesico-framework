package colesico.framework.security.internal;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.key.ClassedKey;
import colesico.framework.ioc.key.Key;
import colesico.framework.security.authentication.AuthenticationContext;
import colesico.framework.security.authentication.AuthenticationManager;
import colesico.framework.security.authentication.Authenticator;
import colesico.framework.security.authentication.AuthenticationResult;

public class AuthenticationManagerImpl implements AuthenticationManager {

    protected final Ioc ioc;

    public AuthenticationManagerImpl(Ioc ioc) {
        this.ioc = ioc;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public AuthenticationResult<?> authenticate(AuthenticationContext context) {
        Key<Authenticator> authIocKey = new ClassedKey<>(Authenticator.class, context.getClass());
        Authenticator<AuthenticationContext> authenticator = ioc.instanceOrNull(authIocKey);
        if (authenticator == null) {
            return AuthenticationResult.failure("Authenticator not found for authentication '" + context.getClass().getCanonicalName() + "'");
        }
        return authenticator.authenticate(context);
    }
}
