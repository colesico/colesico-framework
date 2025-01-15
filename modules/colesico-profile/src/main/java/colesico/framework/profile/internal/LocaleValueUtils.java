package colesico.framework.profile.internal;

import colesico.framework.profile.DefaultProfile;
import colesico.framework.profile.ProfileValueUtils;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class LocaleValueUtils implements ProfileValueUtils<DefaultProfile, Locale> {


    public static LocaleValueUtils of() {
        return new LocaleValueUtils();
    }

    @Override
    public String getName() {
        return DefaultProfile.LOCALE_PROPERTY;
    }

    @Override
    public PropertyKind getKind() {
        return PropertyKind.PREFERENCE;
    }

    @Override
    public Locale getValue(DefaultProfile profile) {
        return profile.getLocale();
    }

    @Override
    public void setValue(DefaultProfile profile, Locale locale) {
        profile.setLocale(locale);
    }

    @Override
    public String toProperty(Locale locale) {
        return locale.toLanguageTag();
    }

    @Override
    public Locale fromProperty(String property) {
        return Locale.forLanguageTag(property);
    }

    @Override
    public byte[] toBytes(Locale locale) {
        return locale.toLanguageTag().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public Locale fromBytes(byte[] bytes) {
        return Locale.forLanguageTag(new String(bytes, StandardCharsets.UTF_8));
    }

}
