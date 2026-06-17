package colesico.framework.profile.internal;

import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileSource;
import colesico.framework.teleapi.dataport.DataPort;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.util.Locale;
import java.util.Optional;

/**
 * {@link DataPort} based profile source default implementation
 */
@Singleton
public class ProfileSourceImpl implements ProfileSource<Profile<String>, String> {

    protected final Provider<DataPort<?, ?>> dataPort;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public ProfileSourceImpl(Provider<DataPort> dataPort) {
        this.dataPort = (Provider) dataPort;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public Optional<Profile<String>> read(String profileId) {
        Profile<String> profile = (Profile<String>) dataPort.get().read(Profile.class, profileId);
        return Optional.ofNullable(profile);
    }

    @Override
    public Profile<String> getDefault(String profileId) {
        return Profile.Default.of(profileId, Locale.getDefault());
    }

    @Override
    public void write(Profile<String> profile) {
        dataPort.get().write(profile, Profile.class);
    }
}