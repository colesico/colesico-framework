package colesico.framework.resource.assist.localization;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;

/**
 * Objective qualifiers values is used to select appropriate localized resource, etc.
 * Typically, these qualifiers are defined based on the profile of the current user.
 * Number of qualifiers and order must be strongly  according
 * to canonical qualifiers number and order  defined in {@link QualifiersDefinition}
 * Any undetermined qualifier values must be null.
 */
public final class ObjectiveQualifiers implements Iterable<String> {

    /**
     * Qualifiers values.
     * Number of qualifiers values and its order must according
     * to canonical qualifiers order.
     * Undetermined qualifier values must be null.
     * <p>
     * Example:
     * ["en", "US"]
     * ["ru","RU",null]
     * [null, "UK"]
     */
    private final String[] values;

    public ObjectiveQualifiers(String[] values) {
        this.values = values;
    }

    public static ObjectiveQualifiers of(String[] values) {
        return new ObjectiveQualifiers(values);
    }

    public static ObjectiveQualifiers of(Qualifier[] qualifiers) {
        String[] values = new String[qualifiers.length];
        for (int i = 0; i < qualifiers.length; i++) {
            values[i] = qualifiers[i].value();
        }
        return new ObjectiveQualifiers(values);
    }

    /**
     * Creates from the given locale for language
     */
    public static ObjectiveQualifiers ofLocaleL(Locale locale) {
        return new ObjectiveQualifiers(new String[]{
                StringUtils.isBlank(locale.getLanguage()) ? null : locale.getLanguage(),
        });
    }

    /**
     * Creates from the given locale for language and country
     */
    public static ObjectiveQualifiers ofLocaleLC(Locale locale) {
        return new ObjectiveQualifiers(new String[]{
                StringUtils.isBlank(locale.getLanguage()) ? null : locale.getLanguage(),
                StringUtils.isBlank(locale.getCountry()) ? null : locale.getCountry()
        });
    }

    /**
     * Creates from the given locale for language, country, variant
     */
    public static ObjectiveQualifiers ofLocaleLCV(Locale locale) {
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
        return "ObjectiveQualifiers{" +
                "values=" + Arrays.toString(values) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ObjectiveQualifiers strings = (ObjectiveQualifiers) o;
        return Objects.deepEquals(values, strings.values);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }
}
