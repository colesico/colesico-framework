package colesico.framework.example.profile.custom;

import colesico.framework.profile.DefaultProfileManager;
import colesico.framework.profile.LocaleAttribute;
import colesico.framework.profile.ProfileAttribute;
import colesico.framework.teleapi.DataPort;

import java.util.Collection;
import java.util.Set;
import java.util.TimeZone;

public class CustomProfileManager extends DefaultProfileManager<CustomProfile> {

    @Override
    public CustomProfile createProfile() {
        return initProfile(new CustomProfile());
    }

    @Override
    protected CustomProfile initProfile(CustomProfile profile) {
        profile = super.initProfile(profile);
        profile.setTimeZone(TimeZone.getDefault());
        return profile;
    }

    @Override
    public Collection<ProfileAttribute> getAttributes(CustomProfile profile) {
        var attributes = super.getAttributes(profile);
        attributes.add(new TimezoneAttribute(profile));
        attributes.add(new ApiVersionAttribute(profile));
        return attributes;
    }

    @Override
    public CustomProfile readProfile(DataPort dataPort) {
        var profile = super.readProfile(dataPort);
        profile.setApiVersion("2.0");
        return profile;
    }
}
