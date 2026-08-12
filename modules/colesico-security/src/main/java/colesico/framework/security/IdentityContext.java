package colesico.framework.security;

import colesico.framework.ioc.key.Key;
import colesico.framework.ioc.key.TypeKey;

import java.util.Optional;

/**
 * Current identity holder
 */
public interface IdentityContext {

    Key<Identity> SCOPE_KEY = new TypeKey<>(Identity.class);

    /**
     * Returns {@link Identity} bound to current scope  (thread, request, etc)
     */
    Optional<Identity<?>> identity();

    void setIdentity(Identity<?> identity);

    /**
     * Remove identity bound to current scope
     */
    void clear();

}
