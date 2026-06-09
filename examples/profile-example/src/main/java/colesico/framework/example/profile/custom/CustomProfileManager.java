package colesico.framework.example.profile.custom;


import colesico.framework.ioc.scope.TaskScope;
import colesico.framework.profile.assist.DefaultProfileManager;
import jakarta.inject.Singleton;

import java.util.Locale;
import java.util.TimeZone;

@Singleton
public class CustomProfileManager extends DefaultProfileManager<CustomProfile> {

    public CustomProfileManager(TaskScope taskScope) {
        super(taskScope);
    }

    @Override
    public CustomProfile createProfile() {
        var profile = new CustomProfile();
        profile.setLocale(Locale.getDefault());
        profile.setTimeZone(TimeZone.getDefault());
        return profile;
    }

    @Override
    protected CustomProfile read() {
        var profile = super.read();
        // Set from another source
        profile.setApiVersion("1.0");
        return profile;
    }
}
