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
    public Collection<Authenticator<?, ?>> findAuthenticators(AuthenticationMessage request) {
        if (request == null) {
            throw new SecurityException("Authentication request is null");
        }
        Key<Authenticator> iocKey = new ClassedKey<>(Authenticator.class, request.getClass());
        List<Authenticator<?, ?>> result = new ArrayList<>();
        ioc.polysupplier(iocKey).forEach(a -> result.add(a));
        return result;
    }

    @Override
    public Optional<Authenticator<?, AuthenticationCallback<?, ?>>> findAuthenticator(Identity<?> identity) {
        var authenticatorIdClass = identity.claim(AUTHENTICATION_ID_CLAIM, Class.class);
        if (authenticatorIdClass.isEmpty()) {
            return Optional.empty();
        }
        Key<Authenticator> iocKey = new ClassedKey<>(Authenticator.class, authenticatorIdClass.get());
        return Optional.of(ioc.instance(iocKey));
    }

    @Override
    public Optional<AuthenticationCallback<?, ?>> findCallback(Identity<?> identity) {
        var callbackIdClass = identity.claim(CALLBACK_CLAIM, Class.class);
        if (callbackIdClass.isEmpty()) {
            return Optional.empty();
        }
        Key<AuthenticationCallback> iocKey = new ClassedKey<>(AuthenticationCallback.class, callbackIdClass.get());
        return Optional.of(ioc.instance(iocKey));
    }
}
