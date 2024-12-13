package colesico.framework.profile.internal;

import colesico.framework.profile.ProfileUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ProfileUtilsImpl implements ProfileUtils<ProfileImpl> {

    @Override
    public ProfileImpl create(Collection<?> attributes, Collection<?> preferences) {
        ProfileImpl profile = new ProfileImpl();
        attributes.forEach(profile::setAttribute);
        preferences.forEach(profile::setAttribute);
        return profile;
    }

    @Override
    public Map<String, String> preferenceTags(ProfileImpl profile) {
        Map<String, String> result = new HashMap<>();
        profile.getPreferences().forEach();
    }

    @Override
    public Map<String, String> attributeTags(ProfileImpl profile) {
        Map<String, String> result = new HashMap<>();
    }
}
