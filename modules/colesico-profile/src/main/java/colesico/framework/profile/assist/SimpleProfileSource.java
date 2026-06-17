package colesico.framework.profile.assist;

import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileException;
import colesico.framework.profile.ProfileSource;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * A simple, in-memory {@link ProfileSource} implementation powered by {@link ConcurrentHashMap}.
 * This class is designed to be extended by specific profile types to fulfill the {@link #getDefault(Object)} contract.
 */
public class SimpleProfileSource<P extends Profile<ID>, ID> implements ProfileSource<P, ID> {

    // Thread-safe store for profiles
    protected final Map<ID, P> profileHolder = new ConcurrentHashMap<>();

    protected final Supplier<P> defaultProfileFactory;

    public SimpleProfileSource(Supplier<P> defaultProfileFactory) {
        this.defaultProfileFactory = defaultProfileFactory;
    }

    public static <P extends Profile<ID>, ID> SimpleProfileSource<P, ID> of(Supplier<P> defaultProfileFactory) {
        return new SimpleProfileSource<>(defaultProfileFactory);
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
    public P getDefault(ID profileId) {
        return defaultProfileFactory.get();
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