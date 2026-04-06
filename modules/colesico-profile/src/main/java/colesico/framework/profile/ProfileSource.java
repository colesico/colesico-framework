package colesico.framework.profile;

/**
 * Profile source API.
 * Represents a source that is used for read/write Profile instance.
 * The Source of the Profile can be anything, for example, a config, database, HTTP/RPC request/response, Kafka message etc.
 * Associates current profile instance to the current thread.
 */
public interface ProfileSource<P extends Profile> {

    /**
     * Read profile from source.
     */
    P read();

    /**
     * Save profile preferences to source
     */
    void write(P profile);

}
