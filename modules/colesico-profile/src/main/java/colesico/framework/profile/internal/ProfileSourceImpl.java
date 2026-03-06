package colesico.framework.profile.internal;

import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.profile.*;
import colesico.framework.teleapi.DataPort;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

@Singleton
public class ProfileSourceImpl implements ProfileSource {

    protected final ProfileManager profileManager;

    protected final Provider<DataPort> dataPortProv;

    // Profile cache
    protected final ThreadScope threadScope;

    public ProfileSourceImpl(ProfileManager profileManager,
                             Provider<DataPort> dataPortProv,
                             ThreadScope threadScope) {

        this.profileManager = profileManager;
        this.dataPortProv = dataPortProv;
        this.threadScope = threadScope;
    }

    @Override
    public Profile read() {
        Profile profile = threadScope.get(Profile.SCOPE_KEY);
        if (profile != null) {
            return profile;
        }
        profile = profileManager.readProfile(dataPortProv.get());
        threadScope.put(Profile.SCOPE_KEY, profile);
        return profile;
    }

    @Override
    public void write(Profile profile) {
        profile = profileManager.writeProfile(profile, dataPortProv.get());
        threadScope.put(Profile.SCOPE_KEY, profile);
    }
}
