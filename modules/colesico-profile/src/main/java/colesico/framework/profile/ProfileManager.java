package colesico.framework.profile;

import java.util.Optional;

/**
 * Profile manager API.
 * Manager can store/obtain profile from different sources,
 * for example, a config, database,
 * HTTP/RPC request/response, Kafka message etc.
 * Manager associates current profile instance to the current thread.
 */
public interface ProfileManager {

    /**
     * Retrieves the profile by ID.
     *
     * <p>Looks up the profile in the {@link ProfileContext} first.
     * If missing, fetches it from the {@link ProfileSource} and
     * caches the result back into the context.
     *
     * @param profileId can be null.
     */
    <P extends Profile<ID>, ID> Optional<P> profile(ID profileId);

    /**
     * Save profile preferences to {@link ProfileSource} and
     * updates in {@link ProfileContext}
     */
    void save(Profile<?> profile);

}
