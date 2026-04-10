package colesico.framework.profile;

/**
 * Profile context API.
 * Represents a context that store Profile instance.
 * The context can store/obtain profile from different sources,
 * for example, a config, database,
 * HTTP/RPC request/response, Kafka message etc.
 * Context associates current profile instance to the current thread.
 */
public interface ProfileContext<P extends Profile> {

    /**
     * Obtain current profile instance.
     * Possible null response
     */
    P get();

    /**
     * Save profile preferences to context
     */
    void set(P profile);

}
