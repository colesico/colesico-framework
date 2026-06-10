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
     * Retrieves the current {@link Profile} from the active {@link ProfileContext}.
     * Returns an empty Optional if no profile in context.
     */
    Optional<Profile> profile();

    /**
     * Save profile preferences
     */
    void save(Profile profile);

}
