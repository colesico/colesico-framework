package colesico.framework.profile.internal;

import colesico.framework.profile.ProfilePreferences;
import colesico.framework.profile.Profile;

import java.util.Iterator;

public class ProfilePreferencesImpl implements ProfilePreferences {

    protected final ProfileImpl profile;

    public ProfilePreferencesImpl(ProfileImpl profile) {
        this.profile = profile;
    }

    @Override
    public boolean isEmpty() {
        return profile.getPreferences().isEmpty();
    }

    @Override
    public <T> boolean contains(Class<T> propertyClass) {
        T property =  profile.get(propertyClass);
        if (property != null) {
            return profile.getPreferences().contains(property);
        } else {
            return false;
        }
    }

    @Override
    public void clear() {
        profile.getPreferences().clear();
    }

    @Override
    public Profile profile() {
        return profile;
    }

    @Override
    public <T> T add(T property) {
        return profile.addPreference(property);
    }

    @Override
    public <T> T exclude(Class<T> propertyClass) {
        T property = (T) profile.get(propertyClass);
        if (property != null) {
            profile.getPreferences().remove(property);
            return property;
        }
        return null;
    }

    @Override
    public Iterator iterator() {
        return profile.getPreferences().iterator();
    }
}
