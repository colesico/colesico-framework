package colesico.framework.profile.assist;

import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileException;
import colesico.framework.profile.ProfileSource;
import jakarta.inject.Singleton;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A simple, in-memory {@link ProfileSource} implementation powered by {@link ConcurrentHashMap}.
 * This class is designed to be extended by specific profile types to fulfill the {@link #createDefault(Object)} contract.
 */
@Singleton
public class SimpleProfileSource<P extends Profile<ID>, ID> implements ProfileSource<P, ID> {

    // Thread-safe store for profiles
    protected final Map<ID, P> profileHolder = new ConcurrentHashMap<>();

    public SimpleProfileSource() {
    }

    @Override
    public Optional<P> read(ID profileId) {
        if (profileId == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(profileHolder.get(profileId));
    }

    /**
     * Subclasses must override this method to create a specific default profile instance.
     * This avoids ClassCastException caused by erasing generic type P.
     */
    @Override
    public P createDefault(ID profileId) {
        return (P) new Profile.Default(profileId, Locale.getDefault());
    }

    @Override
    public void write(P profile) {

        if (profile == null) {
            throw new ProfileException("Profile cannot be null");
        }

        ID id = profile.id();

        if (id != null) {
            profileHolder.put(id, profile);
        }

    }

    @Override
    public void delete(ID profileId) {
        if (profileId != null) {
            profileHolder.remove(profileId);
        }
    }
}