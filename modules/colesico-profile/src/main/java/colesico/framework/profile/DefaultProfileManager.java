package colesico.framework.profile;

import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.teleapi.dataport.DataPort;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.util.Locale;

/**
 * Data port as source for profile
 */
@Singleton
public class DefaultProfileManager extends AbstractProfileManager<DefaultProfile> {

    protected final Provider<DataPort> dataPortProv;

    public DefaultProfileManager(ThreadScope threadScope, Provider<DataPort> dataPortProv) {
        super(threadScope);
        this.dataPortProv = dataPortProv;
    }

    @Override
    public DefaultProfile createProfile() {
        var profile = new DefaultProfile();
        profile.setLocale(Locale.getDefault());
        return profile;
    }

    @Override
    protected DefaultProfile read(DefaultProfile profile) {
        return (DefaultProfile) dataPortProv.get().read(Profile.class, profile);
    }

    @Override
    protected DefaultProfile write(DefaultProfile profile) {
        dataPortProv.get().write(profile, Profile.class);
        return profile;
    }
}
