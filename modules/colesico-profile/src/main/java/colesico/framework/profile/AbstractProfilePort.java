package colesico.framework.profile;

import colesico.framework.profile.ProfileListener.CheckResult;

abstract public class AbstractProfilePort<P extends Profile> implements ProfilePort<P> {

    // Profile cache
    protected P profileCache;

    protected final ProfileListener<P> control;

    public AbstractProfilePort(ProfileListener<P> control) {
        this.control = control;
    }

    abstract protected void writeToSource(P profile);

    abstract protected P readFromSource();

    @Override
    public P read() {
        if (profileCache != null) {
            return profileCache;
        }
        P profile = readFromSource();
        CheckResult<P> result = control.afterRead(profile);
        if (result.refresh()) {
            writeToSource(result.profile());
        }
        profileCache = result.profile();
        return profileCache;
    }

    @Override
    public void write(P profile) {
        profile = control.beforeWrite(profile);
        writeToSource(profile);
        profileCache = null;
    }
}
