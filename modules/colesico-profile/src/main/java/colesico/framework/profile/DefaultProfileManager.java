package colesico.framework.profile;

import colesico.framework.ioc.scope.TaskScope;
import colesico.framework.teleapi.dataport.DataPort;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.util.Locale;

/**
 * Data port as source for profile
 */
@Singleton
public class DefaultProfileManager extends AbstractProfileManager<Profile> {

    protected final Provider<DataPort<?, ?>> dataPort;

    public DefaultProfileManager(TaskScope taskScope, Provider<DataPort> dataPort) {
        super(taskScope);
        this.dataPort = (Provider) dataPort;
    }

    @Override
    public Profile createProfile() {
        return Profile.Default.of(Locale.getDefault());
    }

    @Override
    protected Profile read() {
        return dataPort.get().read(Profile.class, createProfile());
    }

    @Override
    protected void write(Profile profile) {
        dataPort.get().write(profile, Profile.class);
    }
}
