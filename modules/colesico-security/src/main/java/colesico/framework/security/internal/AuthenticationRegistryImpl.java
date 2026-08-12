package colesico.framework.security.internal;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.key.ClassedKey;
import colesico.framework.ioc.key.Key;
import colesico.framework.security.Identity;
import colesico.framework.security.authentication.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static colesico.framework.security.Identity.*;

public class AuthenticationRegistryImpl implements AuthenticationRegistry {

    protected final Ioc ioc;

    public AuthenticationRegistryImpl(Ioc ioc) {
        this.ioc = ioc;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Collection<Authenticator<?, ?>> findAuthenticators(AuthenticationRequest request) {
        if (request == null) {
            throw new SecurityException("Authentication request is null");
        }
        Key<Authenticator> authIocKey = new ClassedKey<>(Authenticator.class, request.getClass());
        List<Authenticator<?, ?>> result = new ArrayList<>();
        ioc.polysupplier(authIocKey).forEach(a -> result.add(a));
        return result;
    }

    @Override
    public Optional<Authenticator<?, AuthenticationCallback<?, ?>>> findAuthenticator(Identity<?> identity) {
        var authenticatorClass = identity.claim(AUTHENTICATOR_CLAIM, Class.class);
        if (authenticatorClass.isPresent()) {
            var authenticator = ioc.instanceOrNull(authenticatorClass.get());
            return Optional.ofNullable((Authenticator) authenticator);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<AuthenticationCallback<?, ?>> findCallback(Identity<?> identity) {
        var sourceClass = identity.claim(CALLBACK_CLAIM, Class.class);
        if (sourceClass.isPresent()) {
            var source = ioc.instanceOrNull(sourceClass.get());
            return Optional.ofNullable((AuthenticationCallback<?, ?>) source);
        } else {
            return Optional.empty();
        }
    }
}
