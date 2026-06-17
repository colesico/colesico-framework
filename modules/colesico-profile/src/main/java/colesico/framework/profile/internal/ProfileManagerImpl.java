package colesico.framework.profile.internal;

import colesico.framework.profile.*;
import colesico.framework.security.Identity;
import colesico.framework.security.IdentityContext;

import java.util.Optional;

public class ProfileManagerImpl implements ProfileManager {

    private final ProfileSource<Profile<Object>, Object> profileSource;
    private final ProfileContext profileContext;
    private final IdentityContext identityContext;

    @SuppressWarnings("unchecked")
    public ProfileManagerImpl(ProfileSource profileSource,
                              ProfileContext profileContext,
                              IdentityContext identityContext) {
        this.profileSource = (ProfileSource<Profile<Object>, Object>) profileSource;
        this.profileContext = profileContext;
        this.identityContext = identityContext;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <P extends Profile<?>> P resolve() {
        // 1. Lookup in the current thread context
        Optional<Profile<?>> currentProfile = profileContext.profile();
        if (currentProfile.isPresent()) {
            return (P) currentProfile.get();
        }

        // 2. Retrieve profile ID from the security context
        Object profileId = identityContext.identity()
                .map(Identity::id)
                .orElse(null);

        // 3. If no identity ID is found, fallback to the default profile
        if (profileId == null) {
            P defaultProfile = (P) profileSource.getDefault(null);
            profileContext.setProfile(defaultProfile);
            return defaultProfile;
        }

        // 4. Fetch the profile from the source (DB, config, cache, etc.)
        Optional<Profile<Object>> fetchedProfile = profileSource.read(profileId);
        if (fetchedProfile.isEmpty()) {
            P defaultProfile = (P) profileSource.getDefault(profileId);
            profileContext.setProfile(defaultProfile);
            return defaultProfile;
        }

        // 5. Cache the resolved profile in the context and return it
        P profile = (P) fetchedProfile.get();
        profileContext.setProfile(profile);
        return profile;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void commit(Profile<?> profile) {
        if (profile == null) {
            throw new ProfileException("Profile cannot be null");
        }
        profileSource.write((Profile<Object>) profile);
        profileContext.setProfile(profile);
    }


    @Override
    public <P extends Profile<?>> P reload() {
        profileContext.clear();
        return resolve();
    }
}