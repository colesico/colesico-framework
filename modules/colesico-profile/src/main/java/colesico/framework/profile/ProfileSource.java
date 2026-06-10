package colesico.framework.profile;

import java.util.Optional;

/**
 * Represents any profile source (DB, config, HTTP Request, Kafka message etc.)
 */
public interface ProfileSource {

    /**
     * Read profile data from source.
     */
    Optional<Profile> read();

    void write(Profile profile);

}
