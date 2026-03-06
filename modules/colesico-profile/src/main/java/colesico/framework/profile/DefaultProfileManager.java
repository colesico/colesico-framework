package colesico.framework.profile;

import colesico.framework.teleapi.DataPort;
import jakarta.inject.Singleton;

import java.util.Collection;
import java.util.Locale;
import java.util.Set;

/**
 * Default profile factory implementation
 */
@Singleton
public class DefaultProfileManager implements ProfileManager<DefaultProfile> {

    @Override
    public DefaultProfile newInstance() {
        return new DefaultProfile();
    }

    @Override
    public Collection<ProfileAttribute> getAttributes(DefaultProfile profile) {
        return Set.of(LocaleAttribute.of(profile));
    }

    @Override
    public DefaultProfile readProfile(DataPort dataPort) {
        var profile = ProfileManager.super.readProfile(dataPort);

        if (profile == null){
            profile = newInstance();
            profile.setLocale(Locale.getDefault());
            return profile;
        }

        if (profile.getLocale() == null) {
            profile.setLocale(Locale.getDefault());
        }

        return profile;
    }
}
