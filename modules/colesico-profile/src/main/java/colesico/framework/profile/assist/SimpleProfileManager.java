package colesico.framework.profile.assist;

import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileSource;
import jakarta.inject.Singleton;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class SimpleProfileSource<P extends Profile<ID>, ID> implements ProfileSource<P, ID> {

    private final Map<ID, P> profileHolder = new ConcurrentHashMap<>();

    public SimpleProfileSource() {
    }

    @Override
    public Optional<P> read(ID profileId) {
        if (profileId != null) {
            return Optional.ofNullable(profileHolder.get(profileId));
        } else {
            return (Optional<P>) Optional.of(new Profile.Default<ID>(null, Locale.getDefault()));
        }
    }

    @Override
    public void write(P profile) {
        profileHolder.put(profile.id(), profile);
    }

    @Override
    public void delete(ID profileId) {
        profileHolder.remove(profileId);
    }
}
