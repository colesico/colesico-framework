package colesico.framework.profile;

import colesico.framework.ioc.key.Key;
import colesico.framework.ioc.key.TypeKey;

import java.util.Optional;

/**
 * Current profile holder.
 */
public interface ProfileContext<ID> {

    /**
     * Key to bind profile to scope
     */
    Key<Profile<?>> SCOPE_KEY = new TypeKey(Profile.class);

    /**
     * Returns {@link Profile} bound to current scope  (thread, request, etc)
     */
    Optional<Profile<ID>> profile();
    Optional<Profile<ID>> profile(ID profileId);

    void setProfile(Profile<ID> profile);

    /**
     * Remove profile bound to current scope
     */
    void clear();

}
