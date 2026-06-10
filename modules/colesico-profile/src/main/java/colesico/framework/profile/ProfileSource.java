package colesico.framework.profile;

import java.util.Optional;

/**
 * Represents any profile source (DB, config, HTTP Request, Kafka message etc.)
 */
public interface ProfileSource<P extends Profile<ID>, ID> {

    /**
     * Read profile data from source.
     * @param profileId possible null value
     */
    Optional<P> read(ID profileId);

    /**
     * Save profile preferences
     */
    void write(P profile);

}
