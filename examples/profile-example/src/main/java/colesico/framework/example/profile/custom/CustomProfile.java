package colesico.framework.example.profile.custom;

import colesico.framework.profile.Profile;

import java.util.TimeZone;

public class CustomProfile extends Profile.Default<Long> {

    protected TimeZone timeZone;
    protected String apiVersion;

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
