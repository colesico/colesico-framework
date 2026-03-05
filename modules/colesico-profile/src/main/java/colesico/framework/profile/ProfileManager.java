package colesico.framework.profile;

import java.util.Collection;

public interface ProfileManager<P extends Profile> {

    /**
     * Create empty profile instance
     */
    P newInstance();

    /**
     * Initialize profile instance with default values
     */
    void initDefault(P profile);

    /**
     * Profile attributes accessors
     */
    Collection<ProfileAttribute> getAttributes(P profile);

    /**
     * Controls the profile after read from data port.
     * Implement this method to get more specific control.
     * This method is used to fine-grained control of profile: check validity,
     * enrich with extra data from database, e.t.c.
     *
     * @return Valid profile or null
     */
    default P afterRead(P profile) {
        return profile;
    }

    /**
     * Controls the profile before write it to data port.
     * Implement this method to get more specific control.
     */
    default P beforeWrite(P profile) {
        return profile;
    }
}
