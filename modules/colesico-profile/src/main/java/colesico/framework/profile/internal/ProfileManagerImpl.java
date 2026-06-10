package colesico.framework.profile.internal;

import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileContext;
import colesico.framework.profile.ProfileManager;
import colesico.framework.profile.ProfileSource;

import java.util.Optional;

public class ProfileManagerImpl implements ProfileManager {

    private final ProfileSource source;
    private final ProfileContext context;

    public ProfileManagerImpl(ProfileSource source, ProfileContext context) {
        this.source = source;
        this.context = context;
    }

    @Override
    public Optional<Profile> profile() {
        var profile = context.profile();
        if (profile.isPresent()) {
            return profile;
        }
        profile = source.read();
        if (profile.isPresent()) {
            context.setProfile(profile().get());
        }
        return profile;
    }

    @Override
    public void save(Profile profile) {
        source.write(profile);
        context.clear();
    }
}
