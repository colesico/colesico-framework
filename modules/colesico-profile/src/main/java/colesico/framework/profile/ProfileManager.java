package colesico.framework.profile;

import colesico.framework.teleapi.DataPort;

import java.util.Collection;

/**
 * Framework level Profile API.
 * Implement this API to customize profile management logic.
 */
public interface ProfileManager<P extends Profile> {

    /**
     * Create profile default instance
     */
    P createProfile();

    /**
     * Profile attributes accessors
     */
    Collection<ProfileAttribute> getAttributes(P profile);

    /**
     * Read profile from phisical source.
     * Implement this method to get more specific read control.
     * This method also is used to fine-grained  read control of profile: check validity,
     * enrich with extra data from database, e.t.c.
     *
     * @return Valid profile or null
     */
    default P readProfile(DataPort dataPort) {
        var profile = (P) dataPort.read(Profile.class);
        return profile != null ? profile : createProfile();
    }

    /**
     * Writes profile to phisical source.
     * Implement this method to get more specific control.
     */
    default P writeProfile(P profile, DataPort dataPort) {
        dataPort.write(profile, Profile.class);
        return profile;
    }
}
