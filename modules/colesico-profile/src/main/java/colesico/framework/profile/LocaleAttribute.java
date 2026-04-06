package colesico.framework.profile;

import java.util.Locale;

/**
 * Locale attribute accessor
 */
public class LocaleAttribute extends AbstractProfileAttribute<Profile, Locale> {

    /**
     * Locale attribute name
     */
    static final String ATTRIBUTE_NAME = "locale";

    public static ProfileAttribute<Locale> of(Profile profile) {
        return new LocaleAttribute(profile);
    }

    public LocaleAttribute(Profile profile) {
        super(profile);
    }

    @Override
    public String name() {
        return ATTRIBUTE_NAME;
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
