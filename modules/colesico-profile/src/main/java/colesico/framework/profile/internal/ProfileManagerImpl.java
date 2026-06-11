package colesico.framework.profile.internal;

import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileContext;
import colesico.framework.profile.ProfileManager;
import colesico.framework.profile.ProfileSource;

import java.util.Optional;

public class ProfileManagerImpl implements ProfileManager {

    @SuppressWarnings("rawtypes")
    private final ProfileSource source;
    private final ProfileContext context;

    @SuppressWarnings("rawtypes")
    public ProfileManagerImpl(ProfileSource source, ProfileContext context) {
        this.source = source;
        this.context = context;
    }


    @Override
    @SuppressWarnings("unchecked")
    public <P extends Profile<ID>, ID> Optional<P> profile(ID profileId) {
        Optional<P> currentProfile = context.profile();
        if (currentProfile.isPresent()) {
            return currentProfile;
        }

        Optional<?> fetched = source.read(profileId);
        if (fetched.isPresent()) {
            P result = (P) fetched.get();
            context.setProfile(result);
            return Optional.of(result);
        }

        return Optional.empty();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void save(Profile<?> profile) {
        source.write(profile);
        context.clear();
    }
}