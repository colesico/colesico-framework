package colesico.framework.profile;

import colesico.framework.ioc.key.Key;
import colesico.framework.ioc.key.TypeKey;
import colesico.framework.profile.internal.ProfileDefaultPort;

/**
 * Represents a port that is used for retrieve Profile instance from the Source and put it back.
 * The Source of the Profile can be anything, for example, a config, database, a HTTP request, Kafka message etc.
 * Profile port instance should be explicitly assign with current thread by
 * putting it to {@link colesico.framework.ioc.scope.ThreadScope}
 * Otherwise {@link ProfileDefaultPort} will be used
 */
public interface ProfilePort<P extends Profile> {

    /**
     * Key for storing instance of ProfilePort in thread scope
     */
    Key<ProfilePort> SCOPE_KEY = new TypeKey<>(ProfilePort.class);

    P read();

    void write(P profile);
}
