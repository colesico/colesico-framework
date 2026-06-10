package colesico.framework.profile;

/**
 * Represents any profile source  (DB, config, HTTP Request, Kafka message etc.)
 */
public interface ProfileSource {

    Profile read();

    void write(Profile profile);

}
