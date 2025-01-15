package colesico.framework.example.profile.custom;

import colesico.framework.config.Config;
import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileConfigPrototype;

import java.util.Locale;
import java.util.TimeZone;

@Config
public class ProfileConfig extends ProfileConfigPrototype {
    @Override
    public Profile profileInstance() {
        CustomProfile profile = new CustomProfile();
        profile.setTimeZone(TimeZone.getDefault());
        profile.setLocale(Locale.getDefault());
        return profile;
    }
}
