package colesico.framework.profile;


/**
 * Represents any profile source (DB, config, HTTP Request, Kafka message etc.)
 */
public interface ProfileSource {

    void populate(Profile.Builder builder);

    void write(Profile profile);

}
