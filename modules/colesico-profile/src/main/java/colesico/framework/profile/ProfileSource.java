package colesico.framework.profile;

/**
 * Application level Profile API.
 * Represents a source that is used for read/write Profile instance.
 * The Source of the Profile can be anything, for example, a config, database, a HTTP/RPC request/response, Kafka message etc.
 * Profile instance should be explicitly assign with current thread by
 * putting it to {@link colesico.framework.ioc.scope.ThreadScope}
 */
public interface ProfileSource<P extends Profile> {

    /**
     * Return current profile
     */
    P read();

    /**
     * Save profile preferences
     */
    void write(P profile);

}
