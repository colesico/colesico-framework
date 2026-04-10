package colesico.framework.profile;

import java.util.Collection;

/**
 * Profile management API.
 * Implement this API to customize profile management logic.
 */
public interface ProfileManager<P extends Profile> {

    /**
     * Create profile default instance
     */
    P createProfile();

    /**
     * Returns profile attributes accessors
     */
    Collection<ProfileAttribute> attributes(P profile);

}
