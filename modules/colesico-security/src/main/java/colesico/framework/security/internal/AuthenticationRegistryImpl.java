package colesico.framework.security.internal;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.key.ClassedKey;
import colesico.framework.ioc.key.Key;
import colesico.framework.ioc.key.NamedKey;
import colesico.framework.security.Identity;
import colesico.framework.security.authentication.AuthenticationRegistry;
import colesico.framework.security.authentication.AuthenticationRequest;
import colesico.framework.security.authentication.Authenticator;
import colesico.framework.security.authentication.LogoutHandler;

import java.util.Optional;

public class AuthenticationRegistryImpl implements AuthenticationRegistry {

    protected final Ioc ioc;

    public AuthenticationRegistryImpl(Ioc ioc) {
        this.ioc = ioc;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Authenticator<AuthenticationRequest> findAuthenticator(AuthenticationRequest request) {
        if (request == null) {
            throw new SecurityException("Authentication request class is null");
        }
        Key<Authenticator> authIocKey = new ClassedKey<>(Authenticator.class, request.getClass());
        var authenticators = ioc.polysupplier(authIocKey);
        for (var authenticator : authenticators) {
            if (authenticator.supports(request)) {
                return authenticator;
            }
        }

        throw new SecurityException("Appropriate authenticator not found for request '" + request + "'");
    }

    @Override
    public Optional<LogoutHandler> findLogoutHandler(Identity<?> identity) {
        var handlerName = identity.logoutHandlerName();
        if (handlerName.isPresent()) {
            var handler = ioc.instanceOrNull(new NamedKey<>(LogoutHandler.class, handlerName.get()));
            return Optional.ofNullable(handler);
        }
        return Optional.empty();
    }
}
