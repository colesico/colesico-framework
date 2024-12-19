package colesico.framework.profile.internal;

import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.profile.*;
import colesico.framework.teleapi.DataPort;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class ProfileSourceImpl implements ProfileSource {

    protected final Provider<DataPort> dataPortProv;

    protected final ProfileListener listener;

    protected final ProfileUtils profileUtils;

    protected final ProfileConfigPrototype config;

    // Profile cache
    protected ThreadScope threadScope;

    public ProfileSourceImpl(Provider<DataPort> dataPortProv, ProfileListener listener, ProfileUtils profileUtils, ProfileConfigPrototype config) {
        this.dataPortProv = dataPortProv;
        this.listener = listener;
        this.profileUtils = profileUtils;
        this.config = config;
    }

    @Override
    public Profile getProfile() {
        Profile profile = threadScope.get(Profile.SCOPE_KEY);
        if (profile != null) {
            return profile;
        }

        profile = (Profile) dataPortProv.get().read(Profile.class);
        if (profile == null) {
            profile = profileUtils.fromAttributes(List.of(config.getDefaultLocale()));
        }

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
