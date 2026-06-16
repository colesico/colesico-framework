package colesico.framework.example.profile.custom;


import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileSource;

import colesico.framework.teleapi.dataport.DataPort;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;

@Singleton
public class CustomProfileSource implements ProfileSource<CustomProfile, Long> {

    protected final Provider<DataPort<?, ?>> dataPort;

    @SuppressWarnings("unchecked")
    public CustomProfileSource(Provider<DataPort> dataPort) {
        this.dataPort = (Provider) dataPort;
    }

    @Override
    public Optional<CustomProfile> read(Long profileId) {
        var profile = (CustomProfile) dataPort.get().read(Profile.class);
        if (profile != null) {
            profile.setApiVersion("2.0");
        }
        return Optional.ofNullable(profile);
    }

    @Override
    public void write(CustomProfile profile) {
        dataPort.get().write(profile, Profile.class);
    }

    @Override
    public CustomProfile createDefault(Long profileId) {
        return new CustomProfile(profileId, Locale.getDefault(), TimeZone.getDefault(), "2.0");
    }
}
