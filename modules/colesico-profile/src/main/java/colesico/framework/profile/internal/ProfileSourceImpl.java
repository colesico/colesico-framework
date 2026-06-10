package colesico.framework.profile.internal;

import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileSource;
import colesico.framework.teleapi.dataport.DataPort;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.util.Optional;

/**
 * {@link DataPort} based profile source implementation
 */
@Singleton
public class ProfileSourceImpl implements ProfileSource {

    protected final Provider<DataPort<?, ?>> dataPort;

    public ProfileSourceImpl(Provider<DataPort> dataPort) {
        this.dataPort = (Provider) dataPort;
    }

    @Override
    public Optional<Profile> read() {
        return Optional.of(dataPort.get().read(Profile.class));
    }

    @Override
    public void write(Profile profile) {
        dataPort.get().write(profile, Profile.class);
    }
}
