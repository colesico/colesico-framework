package colesico.framework.profile.internal;

import colesico.framework.profile.DefaultProfile;
import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileConfigPrototype;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DefaultProfileConfig extends ProfileConfigPrototype {

    @Override
    public Profile createNewProfile() {
        return new DefaultProfile();
    }

    @Override
    public Map<String, Object> createDefaultValues() {
        Map<String, Object> values = new HashMap<>();
        values.put(DefaultProfile.LOCALE_PROPERTY, Locale.getDefault());
        return values;
    }
}
