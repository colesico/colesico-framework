package colesico.framework.profile.internal;

import colesico.framework.profile.ProfileUtils;

import java.util.Collection;
import java.util.List;
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
    public Collection<?> getPreferences(ProfileImpl profile) {
        return profile.getPreferences().values();
    }

    @Override
    public Collection<?> getAttributes(ProfileImpl profile) {
        return profile.getAttributes().values();
    }

    @Override
    public Collection<?> fromTags(Map<String, String> tags) {
        return List.of();
    }

    @Override
    public Map<String, String> toTags(Collection<?> items) {
        return Map.of();
    }

}
