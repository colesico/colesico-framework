package colesico.framework.security.authentication;

import colesico.framework.ioc.key.Key;
import colesico.framework.ioc.key.TypeKey;

/**
 * Current authentications holder
 */
public interface AuthenticationsContext {

    Key<Authentications> SCOPE_KEY = new TypeKey<>(Authentications.class);

    /**
     * Returns {@link AuthenticationSource}s bound to current scope  (thread, request, etc)
     */
    Iterable<Authentication> authentications();

    void setAuthentications(Iterable<Authentication> authentications);

    /**
     * Remove source bound to current scope
     */
    void clear();

    record Authentications(Iterable<Authentication> items) {

    }
}
