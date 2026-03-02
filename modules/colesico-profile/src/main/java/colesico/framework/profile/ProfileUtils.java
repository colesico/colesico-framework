package colesico.framework.profile;

import java.util.Set;

public interface ProfileUtils<P extends Profile> {

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
    Set<ProfileAttribute> getAttributes(P profile);
}
