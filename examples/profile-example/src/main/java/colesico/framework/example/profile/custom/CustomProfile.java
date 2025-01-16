package colesico.framework.example.profile.custom;

import colesico.framework.profile.DefaultProfile;

import java.util.TimeZone;

public class CustomProfile extends DefaultProfile {

    public static final String TIMEZONE_PROPERTY="timezone";

    private TimeZone timeZone;

    // filled in from the listener
    private String apiVersion;

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    @Override
    public String toString() {
        return "CustomProfile{" +
                "locale=" + locale +
                ", timeZone=" + timeZone +
                ", apiVersion='" + apiVersion + '\'' +
                '}';
    }
}
