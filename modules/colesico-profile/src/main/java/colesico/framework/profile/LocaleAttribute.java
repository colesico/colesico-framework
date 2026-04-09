package colesico.framework.profile;

import java.util.Locale;
import java.util.Map;

/**
 * Locale attribute accessor
 */
public class LocaleAttribute extends AbstractProfileAttribute<Profile, Locale> {

    /**
     * Locale attribute name
     */
    static final String ATTRIBUTE_NAME = "locale";
    static final Map<String, Object> METADATA = Map.of();

    public static ProfileAttribute<Profile, Locale> of(Profile profile) {
        return new LocaleAttribute(profile, ATTRIBUTE_NAME, METADATA);
    }

    public static ProfileAttribute<Profile, Locale> of(Profile profile, String name, Map<String, Object> metadata) {
        return new LocaleAttribute(profile, name, metadata);
    }

    public LocaleAttribute(Profile profile, String name, Map<String, Object> metadata) {
        super(profile, name, metadata);
    }

    @Override
    public Locale getValue() {
        return profile.getLocale();
    }

    @Override
    public void setValue(Locale value) {
        profile.setLocale(value);
    }

    @Override
    public String getString() {
        var value = profile.getLocale();
        if (value == null) {
            return null;
        }
        return value.toLanguageTag();
    }

    @Override
    public void setString(String value) {
        if (value == null) {
            return;
        }
        profile.setLocale(Locale.forLanguageTag(value));
    }

}
