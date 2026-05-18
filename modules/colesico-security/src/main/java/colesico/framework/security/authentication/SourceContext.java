package colesico.framework.security.authentication;

import colesico.framework.ioc.key.Key;
import colesico.framework.ioc.key.TypeKey;

/**
 * Current auth sources holder
 */
public interface SourceContext {

    Key<Sources> SCOPE_KEY = new TypeKey<>(Sources.class);

    /**
     * Returns {@link AuthenticationSource}s bound to current scope  (thread, request, etc)
     */
    Iterable<AuthenticationSource> sources();

    void setSources(Iterable<AuthenticationSource> sources);

    /**
     * Remove source bound to current scope
     */
    void clear();

    record Sources(Iterable<AuthenticationSource> items){

    }
}
