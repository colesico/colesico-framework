package colesico.framework.profile;

/**
 * Represents a source that is used for retrieve Profile instance from
 * the Source and put it back for profile preferences.
 * The Source of the Profile can be anything, for example, a config, database, a HTTP request, Kafka message etc.
 * Profile port instance should be explicitly assign with current thread by
 * putting it to {@link colesico.framework.ioc.scope.ThreadScope}
 */
public interface ProfileSource {

    /**
     * Read profile form source.
     * If there is having no profile in the source  - must return default profile with Locale from {@link ProfileConfigPrototype}
     */
    Profile read();

    void write(Profile profile);
}
