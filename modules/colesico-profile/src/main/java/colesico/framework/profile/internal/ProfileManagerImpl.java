package colesico.framework.profile.internal;

import colesico.framework.ioc.scope.RequestScope;
import colesico.framework.profile.AbstractProfileManager;
import colesico.framework.profile.Profile;
import colesico.framework.teleapi.dataport.DataPort;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.util.Locale;

/**
 * Data port as source for profile
 */
@Singleton
public class ProfileManagerImpl extends AbstractProfileManager<Profile> {

    protected final Provider<DataPort> dataPort;

    public ProfileManagerImpl(RequestScope requestScope, Provider<DataPort> dataPort) {
        super(requestScope);
        this.dataPort = dataPort;
    }

    @Override
    public Profile createProfile() {
        return Profile.Default.of(Locale.getDefault());
    }

    @Override
    protected Profile read(Profile profile) {
        return (Profile) dataPort.get().read(Profile.class, profile);
    }

    @Override
    protected Profile write(Profile profile) {
        dataPort.get().write(profile, Profile.class);
        return profile;
    }
}
