package colesico.framework.example.profile.custom;

import colesico.framework.profile.ProfileValueUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.TimeZone;

public class TimezoneValueUtils implements ProfileValueUtils<CustomProfile, TimeZone> {


    @Override
    public String getName() {
        return CustomProfile.TIMEZONE_PROPERTY;
    }

    @Override
    public PropertyKind getKind() {
        return PropertyKind.PREFERENCE;
    }


    @Override
    public TimeZone getValue(CustomProfile profile) {
        return profile.getTimeZone();
    }

    @Override
    public void setValue(CustomProfile profile, TimeZone value) {
        profile.setTimeZone(value);
    }

    @Override
    public String toProperty(TimeZone tz) {
        if (tz == null) {
            return null;
        }
        return tz.getID();
    }

    @Override
    public TimeZone fromProperty(String tzId) {
        if (StringUtils.isBlank(tzId)) {
            return null;
        }
        return TimeZone.getTimeZone(tzId);
    }

    @Override
    public byte[] toBytes(TimeZone tz) {
        if (tz == null) {
            return null;
        }
        return tz.getID().getBytes();
    }

    @Override
    public TimeZone fromBytes(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        String tzId = new String(bytes, StandardCharsets.UTF_8);
        return TimeZone.getTimeZone(tzId);
    }
}
