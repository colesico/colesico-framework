package colesico.framework.profile;

import java.util.Locale;

/**
 * Locale attribute accessor
 */
public class LocaleAttribute extends AbstractProfileAttribute<Profile, Locale, ProfileAttribute.Metadata> {

    /**
     * Locale attribute name
     */
    static final String ATTRIBUTE_NAME = "locale";

    public static ProfileAttribute<Locale,Metadata> of(Profile profile) {
        return new LocaleAttribute(profile, new AttributeMetadata(true, true));
    }

    public static ProfileAttribute<Locale,Metadata> of(Profile profile, Metadata metadata) {
        return new LocaleAttribute(profile, metadata);
    }

    public LocaleAttribute(Profile profile, Metadata metadata) {
        super(profile, metadata);
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
