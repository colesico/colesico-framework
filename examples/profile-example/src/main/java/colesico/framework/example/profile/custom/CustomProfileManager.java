package colesico.framework.example.profile.custom;

import colesico.framework.profile.LocaleAttribute;
import colesico.framework.profile.ProfileAttribute;
import colesico.framework.profile.ProfileManager;

import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

public class CustomProfileManager implements ProfileManager<CustomProfile> {

    @Override
    public CustomProfile newInstance() {
        return new CustomProfile();
    }

    @Override
    public void initDefault(CustomProfile profile) {
        profile.setTimeZone(TimeZone.getDefault());
        profile.setLocale(Locale.getDefault());
    }

    @Override
    public Set<ProfileAttribute> getAttributes(CustomProfile profile) {
        return Set.of(LocaleAttribute.of(profile),
                new TimezoneAttribute(profile),
                new ApiVersionAttribute(profile));
    }

    @Override
    public CustomProfile afterRead(CustomProfile profile) {
        profile.setApiVersion("2.0");
        return profile;
    }
}
