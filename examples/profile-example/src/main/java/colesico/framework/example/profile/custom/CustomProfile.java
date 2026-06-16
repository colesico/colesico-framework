package colesico.framework.example.profile.custom;

import colesico.framework.profile.Profile;

import java.util.Locale;
import java.util.TimeZone;

public class CustomProfile extends Profile.Default<Long> {

    protected TimeZone timeZone;
    protected String apiVersion;

    public CustomProfile() {
    }

    public CustomProfile(Long id, Locale locale, TimeZone timeZone, String apiVersion) {
        super(id, locale);
        this.timeZone = timeZone;
        this.apiVersion = apiVersion;
    }

    public TimeZone timeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public String apiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }
}
