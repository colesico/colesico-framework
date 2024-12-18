package colesico.framework.profile.internal;

import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileListener;
import colesico.framework.profile.ProfileSource;
import colesico.framework.teleapi.DataPort;

import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class ProfileSourceImpl implements ProfileSource {

    protected final Provider<DataPort> dataPortProv;

    protected final ProfileListener listener;

    // Profile cache
    protected ThreadScope threadScope;

    public ProfileSourceImpl(Provider<DataPort> dataPortProv, ProfileListener listener) {
        this.dataPortProv = dataPortProv;
        this.listener = listener;
    }

    @Override
    public Profile read() {
        Profile profile = threadScope.get(Profile.SCOPE_KEY);
        if (profile != null) {
            return profile;
        }

        profile = (Profile) dataPortProv.get().read(Profile.class);
        profile = listener.afterRead(profile);
        threadScope.put(Profile.SCOPE_KEY, profile);

        return profile;
    }

    @Override
    public void write(Profile profile) {
        profile = listener.beforeWrite(profile);
        dataPortProv.get().write(profile, Profile.class);
    }
}
