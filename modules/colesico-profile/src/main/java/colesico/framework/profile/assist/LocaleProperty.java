package colesico.framework.profile.assist;

import colesico.framework.profile.Profile;

import java.util.Locale;

/**
 * Locale property helper
 */
public class LocaleProperty<P extends Profile> extends AbstractProfileProperty<P, Locale> {

    /**
     * Locale property name
     */
    static final String PROPERTY_NAME = "locale";


    public LocaleProperty(P profile, String name) {
        super(profile, name);
    }

    public static <P extends Profile> LocaleProperty<P> of(P profile) {
        return new LocaleProperty<>(profile, PROPERTY_NAME);
    }

    public Locale value() {
        return profile.locale();
    }

    public void setValue(Locale value) {
        profile.setLocale(value);
    }

    public String asString() {
        var value = profile.locale();
        if (value == null) {
            return null;
        }
        return value.toLanguageTag();
    }

    public void setString(String value) {
        if (value == null) {
            return;
        }
        profile.setLocale(Locale.forLanguageTag(value));
    }

}
