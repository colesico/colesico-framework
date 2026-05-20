package colesico.framework.security.internal;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.key.ClassedKey;
import colesico.framework.ioc.key.Key;
import colesico.framework.ioc.key.NamedKey;
import colesico.framework.security.Identity;
import colesico.framework.security.authentication.AuthenticationRegistry;
import colesico.framework.security.authentication.AuthenticationRequest;
import colesico.framework.security.authentication.AuthenticationSource;
import colesico.framework.security.authentication.Authenticator;

import java.util.Optional;

import static colesico.framework.security.Identity.AUTHENTICATOR_CLAIM;
import static colesico.framework.security.Identity.SOURCE_CLAIM;

public class AuthenticationRegistryImpl implements AuthenticationRegistry {

    protected final Ioc ioc;

    public AuthenticationRegistryImpl(Ioc ioc) {
        this.ioc = ioc;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Optional<Authenticator<AuthenticationRequest>> findAuthenticator(AuthenticationRequest request) {
        if (request == null) {
            throw new SecurityException("Authentication request class is null");
        }
        Key<Authenticator> authIocKey = new ClassedKey<>(Authenticator.class, request.getClass());
        var authenticators = ioc.polysupplier(authIocKey);
        for (var authenticator : authenticators) {
            if (authenticator.supports(request)) {
                return Optional.of(authenticator);
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<Authenticator<AuthenticationRequest>> findAuthenticator(Identity<?> identity) {
        var authenticatorClass = identity.claim(AUTHENTICATOR_CLAIM, Class.class);
        if (authenticatorClass.isPresent()) {
            var authenticator = ioc.instanceOrNull(authenticatorClass.get());
            return Optional.ofNullable((Authenticator<AuthenticationRequest>) authenticator);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<AuthenticationSource> findAuthenticationSource(Identity<?> identity) {
        var sourceClass = identity.claim(SOURCE_CLAIM, Class.class);
        if (sourceClass.isPresent()) {
            var source = ioc.instanceOrNull(sourceClass.get());
            return Optional.ofNullable((AuthenticationSource) source);
        } else {
            return Optional.empty();
        }
    }
}
