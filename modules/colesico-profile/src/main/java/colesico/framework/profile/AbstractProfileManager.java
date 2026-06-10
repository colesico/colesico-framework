package colesico.framework.profile;

import colesico.framework.ioc.key.Key;
import colesico.framework.ioc.key.TypeKey;
import colesico.framework.ioc.scope.TaskScope;

abstract public class AbstractProfileManager<P extends Profile> implements ProfileManager<P> {

    /**
     * Profile association with thread
     */
    protected final TaskScope taskScope;

    public AbstractProfileManager(TaskScope taskScope) {
        this.taskScope = taskScope;
    }

    /**
     * Default profile instance factory
     */
    abstract protected P createProfile();

    /**
     * Read profile data from source.
     * Implement this method to fine-grained profile read: check validity,
     * enrich with extra data from database, e.t.c.
     */
    abstract protected P read();

    /**
     * Writes profile to source.
     * Implement this method to get more specific control.
     */
    abstract protected void write(P profile);

    @Override
    public P profile() {
        ProfileHolder holder = taskScope.get(ProfileHolder.SCOPE_KEY);
        if (holder != null) {
            return holder.profile != null ? (P) holder.profile : createProfile();
        } else {
            // Recursive calls protection
            taskScope.put(ProfileHolder.SCOPE_KEY, new ProfileHolder(null));
        }

        // No profile in cache. Retrieve profile from source
        P profile = read();
        if (profile == null) {
            profile = createProfile();
        }
        taskScope.put(ProfileHolder.SCOPE_KEY, new ProfileHolder(profile));
        return profile;
    }

    @Override
    public void save(P profile) {
        write(profile);
        taskScope.put(ProfileHolder.SCOPE_KEY, new ProfileHolder(profile));
    }

    public record ProfileHolder(Profile profile) {
        /**
         * Thread scope key for caching profile
         */
        public static final Key<ProfileHolder> SCOPE_KEY = new TypeKey<>(ProfileHolder.class);
    }

    @Override
    public P refresh() {
        taskScope.remove(ProfileHolder.SCOPE_KEY);
        return profile();
    }
}
