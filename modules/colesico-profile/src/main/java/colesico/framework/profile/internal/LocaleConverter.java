package colesico.framework.profile.internal;

import colesico.framework.profile.PropertyConverter;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class LocaleConverter implements PropertyConverter<Locale> {

    public static String TAG_KEY = "locale";

    public static LocaleConverter of() {
        return new LocaleConverter();
    }

    @Override
    public String getTagKey() {
        return TAG_KEY;
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
    public byte[] toBytes(Locale property) {
        return property.toLanguageTag().getBytes();
    }

    @Override
    public Locale fromBytes(byte[] propertyBytes) {
        return Locale.forLanguageTag(new String(propertyBytes, StandardCharsets.UTF_8));
    }
}
