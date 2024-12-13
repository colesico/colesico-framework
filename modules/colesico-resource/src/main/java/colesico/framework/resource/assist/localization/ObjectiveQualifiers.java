package colesico.framework.resource.assist.localization;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;

/**
 * Objective qualifier values is used to select appropriate localized resource, etc.
 * Typically these qualifiers are defined based on the profile of the current user.
 */
public final class ObjectiveQualifiers implements Iterable<String> {

    /**
     * Qualifiers
     * Number of qualifiers and its order must be in accordance with the according to canonical qualifiers order
     * Undetermined qualifier values must be null.
     */
    private final String[] values;

    public ObjectiveQualifiers(String[] values) {
        this.values = values;
    }

    /**
     * Creates from the given locale for language
     */
    public static ObjectiveQualifiers fromLocaleL(Locale locale) {
        return new ObjectiveQualifiers(new String[]{
                StringUtils.isBlank(locale.getLanguage()) ? null : locale.getLanguage(),
        });
    }

    /**
     * Creates from the given locale for language and country
     */
    public static ObjectiveQualifiers fromLocaleLC(Locale locale) {
        return new ObjectiveQualifiers(new String[]{
                StringUtils.isBlank(locale.getLanguage()) ? null : locale.getLanguage(),
                StringUtils.isBlank(locale.getCountry()) ? null : locale.getCountry()
        });
    }

    /**
     * Creates from the given locale for language, country, variant
     */
    public static ObjectiveQualifiers fromLocaleLCV(Locale locale) {
        return new ObjectiveQualifiers(new String[]{
                StringUtils.isBlank(locale.getLanguage()) ? null : locale.getLanguage(),
                StringUtils.isBlank(locale.getCountry()) ? null : locale.getCountry(),
                StringUtils.isBlank(locale.getVariant()) ? null : locale.getVariant()
        });
    }

    public String[] getValues() {
        return values;
    }

    @Override
    public Iterator<String> iterator() {
        return Arrays.stream(values).iterator();
    }

    @Override
    public String toString() {
        return "ProfileQualifiers{" +
                "values=" + Arrays.toString(values) +
                '}';
    }
}
