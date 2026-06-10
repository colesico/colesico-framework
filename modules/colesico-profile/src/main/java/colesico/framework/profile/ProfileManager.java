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
     * Retrieves the current {@link Profile}.
     * First try to get profile from {@link ProfileContext}
     * then from {@link ProfileSource}
     */
    Optional<Profile> profile();

    /**
     * Save profile preferences to {@link ProfileSource} and
     * updates in active {@link ProfileContext}
     */
    void save(Profile profile);

}
