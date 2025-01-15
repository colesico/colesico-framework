package colesico.framework.profile.internal;

import colesico.framework.profile.DefaultProfile;
import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileConfigPrototype;

import java.util.Locale;

public class DefaultProfileConfig extends ProfileConfigPrototype {

    @Override
    public Profile profileInstance() {
        DefaultProfile profile = new DefaultProfile();
        profile.setLocale(Locale.getDefault());
        return profile;
    }

}
