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
        // Lookup in the current thread context
        Optional<Profile<?>> currentProfile = profileContext.profile();
        if (currentProfile.isPresent()) {
            return (P) currentProfile.get();
        }

        // Retrieve profile ID from the security context as identity id
        Object profileId = identityContext.identity()
                .map(Identity::id)
                .orElse(null);

        // Fetch the profile from the source (DB, config, cache, etc.)
        Optional<Profile<Object>> fetchedProfile = profileSource.read(profileId);
        if (fetchedProfile.isEmpty()) {
            P defaultProfile = (P) profileSource.getDefault(profileId);
            profileContext.setProfile(defaultProfile);
            return defaultProfile;
        }

        // Cache the resolved profile in the context and return it
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