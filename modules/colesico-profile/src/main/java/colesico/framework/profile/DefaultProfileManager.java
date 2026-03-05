package colesico.framework.profile;

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
    public void initDefault(DefaultProfile profile) {
        profile.setLocale(Locale.getDefault());
    }

    @Override
    public Collection<ProfileAttribute> getAttributes(DefaultProfile profile) {
        return Set.of(LocaleAttribute.of(profile));
    }

}
