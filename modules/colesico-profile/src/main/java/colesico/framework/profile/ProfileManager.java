package colesico.framework.profile;

import colesico.framework.teleapi.DataPort;

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
    Collection<ProfileAttribute> getAttributes(P profile);

    /**
     * Read profile from data port.
     * Implement this method to get more specific read control.
     * This method also is used to fine-grained  read control of profile: check validity,
     * enrich with extra data from database, e.t.c.
     * If the profile is not read form data port, it should return the default instance.
     */
    default P readProfile(DataPort dataPort) {
        var profile = (P) dataPort.read(Profile.class);
        return profile != null ? profile : createProfile();
    }

    /**
     * Writes profile to data port.
     * Implement this method to get more specific control.
     */
    default P writeProfile(P profile, DataPort dataPort) {
        dataPort.write(profile, Profile.class);
        return profile;
    }
}
