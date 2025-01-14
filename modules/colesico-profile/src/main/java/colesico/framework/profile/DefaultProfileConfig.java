package colesico.framework.profile;

import java.util.Locale;

public class DefaultProfileConfig extends ProfileConfigPrototype {

    @Override
    public Profile instance() {
        DefaultProfile profile = new DefaultProfile();
        profile.setLocale(Locale.getDefault());
        return profile;
    }

}
