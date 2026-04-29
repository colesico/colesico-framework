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
public class DefaultProfileContext extends AbstractProfileContext<DefaultProfile> {

    protected final Provider<DataPort> dataPortProv;

    public DefaultProfileContext(ThreadScope threadScope, Provider<DataPort> dataPortProv) {
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
    protected DefaultProfile read() {
        return (DefaultProfile) dataPortProv.get().read(Profile.class);
    }

    @Override
    protected DefaultProfile write(DefaultProfile profile) {
        dataPortProv.get().write(profile, Profile.class);
        return profile;
    }
}
