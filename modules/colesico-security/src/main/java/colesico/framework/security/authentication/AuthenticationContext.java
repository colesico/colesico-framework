package colesico.framework.security.authentication;

import colesico.framework.ioc.key.Key;
import colesico.framework.ioc.key.TypeKey;

/**
 * Current authenticators holder
 * assigned from {@link Authentication#value()}
 */
public interface AuthenticationContext {

    Key<Authenticators> SCOPE_KEY = new TypeKey<>(Authenticators.class);

    /**
     * Returns {@link Authenticator}s bound to current scope  (thread, request, etc)
     */
    Iterable<Authenticator<?, ?>> authenticators();

    void setAuthenticators(Iterable<Authenticator<?, ?>> authenticators);

    /**
     * Remove source bound to current scope
     */
    void clear();

    record Authenticators(Iterable<Authenticator<?, ?>> items) {

    }
}
