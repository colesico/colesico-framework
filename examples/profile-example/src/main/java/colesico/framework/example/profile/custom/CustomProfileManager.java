package colesico.framework.example.profile.custom;


import colesico.framework.ioc.scope.TaskScope;
import colesico.framework.profile.assist.SimpleProfileManager;
import jakarta.inject.Singleton;

import java.util.Locale;
import java.util.TimeZone;

@Singleton
public class CustomProfileManager extends SimpleProfileManager<CustomProfile> {

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
