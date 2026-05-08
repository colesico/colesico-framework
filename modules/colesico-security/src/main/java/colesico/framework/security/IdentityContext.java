package colesico.framework.security;

import colesico.framework.ioc.key.Key;
import colesico.framework.ioc.key.TypeKey;

/**
 * Current identity holder
 */
public interface IdentityContext {

    Holder holder();

    void setIdentity(Identity<?> identity);

    void clear();

    record Holder(Identity<?> identity) {
        public static final Key<Holder> SCOPE_KEY = new TypeKey<>(Holder.class);
    }
}
