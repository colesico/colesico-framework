package colesico.framework.profile;

/**
 * Profile manager API.
 * Manager can store/obtain profile from different sources,
 * for example, a config, database,
 * HTTP/RPC request/response, Kafka message etc.
 * Manager associates current profile instance to the current thread.
 */
public interface ProfileManager<P extends Profile> {

    /**
     * Obtain current profile instance.
     * Returns not null value.
     */
    P profile();

    /**
     * Save profile preferences
     */
    void save(P profile);

}
