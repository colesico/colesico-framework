package colesico.framework.profile.internal;

import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.profile.*;
import colesico.framework.profile.ProfileListener;
import colesico.framework.teleapi.DataPort;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

@Singleton
public class ProfileSourceImpl implements ProfileSource {

    protected final ProfileUtils profileUtils;

    protected final ProfileListener sourceListener;

    protected final Provider<DataPort> dataPortProv;

    // Profile cache
    protected final ThreadScope threadScope;

    public ProfileSourceImpl(ProfileUtils profileUtils,
                             ProfileListener sourceListener,
                             Provider<DataPort> dataPortProv,
                             ThreadScope threadScope) {

        this.profileUtils = profileUtils;
        this.sourceListener = sourceListener;
        this.dataPortProv = dataPortProv;
        this.threadScope = threadScope;
    }

    @Override
    public Profile read() {
        Profile profile = threadScope.get(Profile.SCOPE_KEY);
        if (profile != null) {
            return profile;
        }

        profile = (Profile) dataPortProv.get().read(Profile.class);
        if (profile == null) {
            profile = profileUtils.newInstance();
            profileUtils.initDefault(profile);
        }

        profile = sourceListener.afterRead(profile);
        threadScope.put(Profile.SCOPE_KEY, profile);

        return profile;
    }

    @Override
    public void write(Profile profile) {
        profile = sourceListener.beforeWrite(profile);
        dataPortProv.get().write(profile, Profile.class);
        threadScope.put(Profile.SCOPE_KEY, profile);
    }
}
