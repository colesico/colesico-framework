package colesico.framework.example.profile.custom;

import colesico.framework.assist.StringUtils;

import java.util.TimeZone;

public class TimeZoneAttribute extends AbstractProfileAttribute<CustomProfile, TimeZone> {

    static final String ATTRIBUTE_NAME = "time_zone";

    public TimeZoneAttribute(CustomProfile profile, String name) {
        super(profile, name);
    }

    @Override
    public String name() {
        return ATTRIBUTE_NAME;
    }

    @Override
    public TimeZone value() {
        return profile.timeZone();
    }

    @Override
    public void setValue(TimeZone value) {
        profile.setTimeZone(value);
    }

    @Override
    public String asString() {
        if (profile.timeZone() == null) {
            return null;
        }
        return profile.timeZone().getID();
    }

    @Override
    public void setString(String value) {
        if (StringUtils.isBlank(value)) {
            return;
        }
        profile.setTimeZone(TimeZone.getTimeZone(value));
    }

}
