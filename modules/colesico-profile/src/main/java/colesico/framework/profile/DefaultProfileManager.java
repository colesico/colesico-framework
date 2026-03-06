package colesico.framework.profile;

import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

/**
 * Default profile factory implementation
 */
@Singleton
public class DefaultProfileManager<P extends DefaultProfile> implements ProfileManager<P> {

    @Override
    public P createProfile() {
        return initProfile((P) new DefaultProfile());
    }

    protected P initProfile(P profile) {
        profile.setLocale(Locale.getDefault());
        return profile;
    }

    @Override
    public Collection<ProfileAttribute> getAttributes(P profile) {
        var attributes = new ArrayList<ProfileAttribute>();
        attributes.add(LocaleAttribute.of(profile));
        return attributes;
    }

}
