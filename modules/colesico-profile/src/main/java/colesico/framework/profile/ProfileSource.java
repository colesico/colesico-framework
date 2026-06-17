package colesico.framework.profile;

import java.util.Optional;

/**
 * Represents any profile source (DB, config, HTTP Request, Kafka message etc.)
 */
public interface ProfileSource<P extends Profile<ID>, ID> {

    /**
     * Reads profile within source. If not found returns {@link Optional#empty()}
     */
    Optional<P> read(ID profileId);

    /**
     * Create/save profile preferences into source
     */
    void write(P profile);

    /**
     * Delete specified profile from source
     */
    default void delete(ID profileId) {
        // nop
    }

    /**
     * Returns default profile instance
     *
     * @param profileId optional (can be null)
     */
    P getDefault(ID profileId);

}
