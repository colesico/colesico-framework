package colesico.framework.profile.assist;

import colesico.framework.profile.Profile;

import java.util.Locale;

/**
 * Locale attribute helper
 */
public class LocaleAttribute<P extends Profile> extends AbstractProfileAttribute<P, Locale> {

    /**
     * Locale attribute name
     */
    static final String ATTRIBUTE_NAME = "locale";


    public LocaleAttribute(P profile, String name) {
        super(profile, name);
    }

    public static <P extends Profile> LocaleAttribute<P> of(P profile) {
        return new LocaleAttribute<>(profile, ATTRIBUTE_NAME);
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
