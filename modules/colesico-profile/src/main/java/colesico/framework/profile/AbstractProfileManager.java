package colesico.framework.profile;

import colesico.framework.ioc.key.Key;
import colesico.framework.ioc.key.TypeKey;
import colesico.framework.ioc.scope.ThreadScope;

abstract public class AbstractProfileManager<P extends Profile> implements ProfileManager<P> {

    /**
     * Profile association with thread
     */
    protected final ThreadScope threadScope;

    public AbstractProfileManager(ThreadScope threadScope) {
        this.threadScope = threadScope;
    }

    /**
     * Default profile instance factory
     */
    abstract protected P createProfile();

    /**
     * Read profile from source.
     * Implement this method to fine-grained profile read: check validity,
     * enrich with extra data from database, e.t.c.
     */
    abstract protected P read(P profile);

    /**
     * Writes profile to source.
     * Implement this method to get more specific control.
     */
    abstract protected P write(P profile);

    @Override
    public P profile() {
        ProfileHolder holder = threadScope.get(ProfileHolder.SCOPE_KEY);
        if (holder != null) {
            return holder.profile != null ? (P) holder.profile : createProfile();
        } else {
            // Recursive calls protection
            threadScope.put(ProfileHolder.SCOPE_KEY, new ProfileHolder(null));
        }
        // No profile in cache. Retrieve profile from source
        P profile = read(createProfile());
        if (profile == null) {
            throw new ProfileException("Read profile null result");
        }
        threadScope.put(ProfileHolder.SCOPE_KEY, new ProfileHolder(profile));
        return profile;
    }

    @Override
    public void save(P profile) {
        profile = write(profile);
        threadScope.put(ProfileHolder.SCOPE_KEY, new ProfileHolder(profile));
    }

    public record ProfileHolder(Profile profile) {
        /**
         * Thread scope key for caching profile
         */
        public static final Key<ProfileHolder> SCOPE_KEY = new TypeKey<>(ProfileHolder.class);
    }

}
