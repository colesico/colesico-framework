package colesico.framework.resource.assist.localization;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Definition of  all possible qualifier names and its canonical order.
 * Order of qualifiers is important due {@link Matcher} process that defines look up
 * order of qualifiers. The canonical order of qualifiers essentially determines their priority when determining
 * the best match of subject qualifiers to objective ones.
 */
public final class QualifiersDefinition implements Iterable<String> {

    /**
     * QualifiersDefinition for language
     */
    public static final QualifiersDefinition QUALIFIERS_L =
            QualifiersDefinition.of(new String[]{Qualifier.LANGUAGE_QUALIFIER});

    /**
     * QualifiersDefinition for language and country
     */
    public static final QualifiersDefinition QUALIFIERS_LC =
            QualifiersDefinition.of(new String[]{Qualifier.LANGUAGE_QUALIFIER, Qualifier.COUNTRY_QUALIFIER});

    /**
     * QualifiersDefinition for language,country, variant
     */
    public static final QualifiersDefinition QUALIFIERS_LCV =
            QualifiersDefinition.of(new String[]{Qualifier.LANGUAGE_QUALIFIER, Qualifier.COUNTRY_QUALIFIER, Qualifier.VARIANT_QUALIFIER});

    /**
     * Qualifiers names in canonical order
     */
    private final String[] names;

    public QualifiersDefinition(String[] names) {
        this.names = names;
    }

    public static QualifiersDefinition of(String[] names) {
        return new QualifiersDefinition(names);
    }

    public String[] getNames() {
        return names;
    }

    public int getSize() {
        return names.length;
    }

    public String getName(int i) {
        return names[i];
    }

    /**
     * Return qualifiers values in canonical order
     */
    public String[] toValues(Map<String, String> qualifiers) {
        String[] values = new String[names.length];
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            String value = qualifiers.get(name);
            values[i] = value;
        }
        return values;
    }

    /**
     * Return qualifiers values in canonical order
     */
    public String[] toValues(Qualifier[] qualifiers) {
        Map<String, String> qmap = new HashMap<>();
        for (Qualifier q : qualifiers) {
            qmap.put(q.name(), q.value());
        }
        return toValues(qmap);
    }

    @Override
    public Iterator<String> iterator() {
        return Arrays.stream(names).iterator();
    }

    @Override
    public String toString() {
        return "QualifiersDefinition{" +
                "names=" + Arrays.toString(names) +
                '}';
    }
}
