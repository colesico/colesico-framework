package colesico.framework.example.profile.custom;


import colesico.framework.profile.ProfileSource;

import colesico.framework.teleapi.dataport.DataPort;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class CustomProfileSource implements ProfileSource<CustomProfile, Long> {

    protected final Provider<DataPort<?, ?>> dataPort;

    @SuppressWarnings("unchecked")
    public CustomProfileSource(Provider<DataPort> dataPort) {
        this.dataPort = (Provider) dataPort;
    }

    @Override
    public Optional<CustomProfile> read(Long profileId) {
        var profile = dataPort.get().read(CustomProfile.class);
        profile.setApiVersion("1.0");
        return Optional.of(profile);
    }

    @Override
    public void write(CustomProfile profile) {
        dataPort.get().write(profile, CustomProfile.class);
    }
}
