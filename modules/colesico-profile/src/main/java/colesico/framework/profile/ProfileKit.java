package colesico.framework.profile;

import java.util.Collection;
import java.util.List;

/**
 * Represents a source that is used for retrieve Profile instance from
 * the Source and put it back for profile preferences.
 * The Source of the Profile can be anything, for example, a config, database, a HTTP request, Kafka message etc.
 * Profile port instance should be explicitly assign with current thread by
 * putting it to {@link colesico.framework.ioc.scope.ThreadScope}
 */
public interface ProfileKit {

    /**
     * Obtain profile form source.
     * If there is having no profile in the source  - must return default profile with Locale from {@link ProfileConfigPrototype}
     */
    Profile profile();

    /**
     * Create preferences to control preferred profile properties
     */
    ProfilePreferences preferences();

    /**
     * Writes profile preferences to data port
     */
    void commit(ProfilePreferences preferences);

    default void commit(Collection<?> properties) {
        ProfilePreferences preferences = preferences();
        for (Object prop : properties) {
            preferences.add(prop);
        }
        commit(preferences);
    }

    default void commitAll(Object... properties) {
        commit(List.of(properties));
    }

}
