package colesico.framework.example.profile.custom;

import org.apache.commons.lang3.StringUtils;

import java.util.TimeZone;

public class TimezoneAttribute extends AbstractProfileAttribute<CustomProfile, TimeZone> {

    static final String ATTRIBUTE_NAME = "timezone";

    public TimezoneAttribute(CustomProfile profile) {
        super(profile);
    }

    @Override
    public String name() {
        return ATTRIBUTE_NAME;
    }

    @Override
    public TimeZone getValue() {
        return profile.getTimeZone();
    }

    @Override
    public void setValue(TimeZone value) {
        profile.setTimeZone(value);
    }

    @Override
    public String getString() {
        if (profile.getTimeZone() == null) {
            return null;
        }
        return profile.getTimeZone().getID();
    }

    @Override
    public void setString(String value) {
        if (StringUtils.isBlank(value)) {
            return;
        }
        profile.setTimeZone(TimeZone.getTimeZone(value));
    }

}
