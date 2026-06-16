package colesico.framework.profile;


import colesico.framework.security.Identity;

/**
 * Profile manager API.
 * Manager can store/obtain profile from different sources,
 * for example, a config, database,
 * HTTP/RPC request/response, Kafka message etc.
 * Manager associates current profile instance to the current thread.
 */
public interface ProfileManager {

    /**
     * Retrieves the current profile.
     * <p>
     * Resolution order:
     * 1. Looks up in {@link ProfileContext} .
     * 2. If missing, fetches from {@link ProfileSource} by {@link Identity#id()}.
     * 3. If still missing, creates a default profile.
     * <p>
     * The resolved profile is cached back to the context.
     */
    <P extends Profile<?>> P resolve();

    /**
     * Save current profile preferences to {@link ProfileSource} and
     * updates in {@link ProfileContext}
     */
    void commit(Profile<?> profile);

}
