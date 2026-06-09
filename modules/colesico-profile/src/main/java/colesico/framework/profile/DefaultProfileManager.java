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
public class DefaultProfileManager<P extends Profile> extends AbstractProfileManager<P> {

    protected final Provider<DataPort> dataPort;

    public DefaultProfileManager(TaskScope taskScope, Provider<DataPort> dataPort) {
        super(taskScope);
        this.dataPort = dataPort;
    }

    @Override
    @SuppressWarnings("unchecked")
    public P createProfile() {
        return (P) Profile.Default.of(Locale.getDefault());
    }

    @Override
    @SuppressWarnings("unchecked")
    protected P read() {
        return (P) dataPort.get().read(Profile.class, createProfile());
    }

    @Override
    @SuppressWarnings("unchecked")
    protected P write(Profile profile) {
        dataPort.get().write(profile, Profile.class);
        return (P)profile;
    }
}
