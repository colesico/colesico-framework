package colesico.framework.profile;

import colesico.framework.ioc.scope.ThreadScope;

abstract public class AbstractProfileContext<P extends Profile> implements ProfileContext<P> {

    protected final ProfileManager<P> profileManager;

    /**
     * Profile association with thread
     */
    protected final ThreadScope threadScope;

    public AbstractProfileContext(ProfileManager profileManager, ThreadScope threadScope) {
        this.profileManager = profileManager;
        this.threadScope = threadScope;
    }

    /**
     * Read profile from source.
     * Override this method to fine-grained profile read control: check validity,
     * enrich with extra data from database, e.t.c.
     */
    abstract protected P read();

    /**
     * Writes profile to source.
     * Override this method to get more specific control.
     */
    abstract protected P write(P profile);

    @Override
    public P get() {
        P profile = (P) threadScope.get(Profile.SCOPE_KEY);
        if (profile != null) {
            return profile;
        }
        profile = read();
        if (profile == null) {
            profile = profileManager.createProfile();
        }
        threadScope.put(Profile.SCOPE_KEY, profile);
        return profile;
    }

    @Override
    public void set(P profile) {
        profile = write(profile);
        threadScope.put(Profile.SCOPE_KEY, profile);
    }
}
