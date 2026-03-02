package colesico.framework.profile;

import jakarta.inject.Singleton;

import java.util.Locale;
import java.util.Set;

/**
 * Default profile factory implementation
 */
@Singleton
public class DefaultProfileUtils implements ProfileUtils<DefaultProfile> {

    @Override
    public DefaultProfile newInstance() {
        return new DefaultProfile();
    }

    @Override
    public void initDefault(DefaultProfile profile) {
        profile.setLocale(Locale.getDefault());
    }

    @Override
    public Set<ProfileAttribute> getAttributes(DefaultProfile profile) {
        return Set.of(LocaleAttribute.of(profile));
    }

}
