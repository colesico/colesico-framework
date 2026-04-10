package colesico.framework.profile;

import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.teleapi.DataPort;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

@Singleton
public class DefaultProfileContext extends AbstractProfileContext<AbstractProfile> {

    protected final Provider<DataPort> dataPortProv;

    public DefaultProfileContext(ProfileManager profileManager, ThreadScope threadScope, Provider<DataPort> dataPortProv) {
        super(profileManager, threadScope);
        this.dataPortProv = dataPortProv;
    }

    @Override
    protected AbstractProfile read() {
        return (AbstractProfile) dataPortProv.get().read(Profile.class);
    }

    @Override
    protected AbstractProfile write(AbstractProfile profile) {
        dataPortProv.get().write(profile, Profile.class);
        return profile;
    }
}
