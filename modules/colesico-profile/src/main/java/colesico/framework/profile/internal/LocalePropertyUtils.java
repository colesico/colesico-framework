package colesico.framework.profile.internal;

import colesico.framework.profile.DefaultProfile;
import colesico.framework.profile.PropertyUtils;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class LocalePropertyUtils implements PropertyUtils<DefaultProfile, Locale> {


    public static LocalePropertyUtils of() {
        return new LocalePropertyUtils();
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
    public String toTag(Locale locale) {
        return locale.toLanguageTag();
    }

    @Override
    public Locale fromTag(String tag) {
        return Locale.forLanguageTag(tag);
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
