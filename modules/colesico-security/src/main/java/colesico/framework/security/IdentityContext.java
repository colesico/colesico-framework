package colesico.framework.security;

import colesico.framework.ioc.key.Key;
import colesico.framework.ioc.key.TypeKey;

/**
 * Current identity holder
 */
public interface IdentityContext {

    /**
     * Returns entry bound to current scope  (thread, request, etc)
     */
    Entry get();

    void setEntry(Entry entry);

    default void setIdentity(Identity<?> identity) {
        setEntry(new Entry(identity));
    }

    /**
     * Remove entry  bound to current scope
     */
    void clear();

    /**
     * Context data holder
     */
    record Entry(Identity<?> identity) {
        public static final Key<Entry> SCOPE_KEY = new TypeKey<>(Entry.class);
    }
}
