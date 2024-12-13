package colesico.framework.profile.internal;


import colesico.framework.profile.AbstractProfilePort;
import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileListener;

import javax.inject.Singleton;
import java.util.Locale;

/**
 * Profile default port.
 * Is used when no port explicitly assigned for current thread
 */
@Singleton
public class ProfileDefaultPort extends AbstractProfilePort {

    public ProfileDefaultPort(ProfileListener control) {
        super(control);
    }

    @Override
    protected void writeToSource(Profile profile) {
        Locale.setDefault(profile.getLocale());
    }

    @Override
    protected Profile readFromSource() {
        return control.create(Locale.getDefault());
    }

}
