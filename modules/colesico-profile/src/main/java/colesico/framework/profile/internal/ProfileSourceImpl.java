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
public class ProfileSourceImpl<P extends Profile<ID>, ID> implements ProfileSource<P, ID> {

    protected final Provider<DataPort<?, ?>> dataPort;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public ProfileSourceImpl(Provider<DataPort> dataPort) {
        this.dataPort = (Provider) dataPort;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<P> read(ID profileId) {
        Profile<?> profile = dataPort.get().read(Profile.class);
        return Optional.ofNullable((P) profile);
    }

    @Override
    public void write(P profile) {
        dataPort.get().write(profile, Profile.class);
    }

}