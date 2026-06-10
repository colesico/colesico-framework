package colesico.framework.profile;

import colesico.framework.ioc.key.Key;
import colesico.framework.ioc.key.TypeKey;

/**
 * Current profile holder
 */
public interface ProfileContext {

    Key<Profile> SCOPE_KEY = new TypeKey<>(Profile.class);

    /**
     * Returns {@link Profile} bound to current scope  (thread, request, etc)
     */
    Profile profile();

    void setProfile(Profile profile);

    /**
     * Remove profile bound to current scope
     */
    void clear();

}
