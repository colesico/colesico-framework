package colesico.framework.resource.assist.localization;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Definition of  all possible qualifier names and its canonical order.
 * Order of qualifiers is important due {@link Localizer} process that defines look up
 * order of qualifiers. The canonical order of qualifiers essentially determines their priority when determining
 * the best match of subject qualifiers to objective ones.
 */
public final class QualifiersDefinition implements Iterable<String> {

    /**
     * QualifiersDefinition for language
     */
    private static final QualifiersDefinition L_QUALIFIERS = new QualifiersDefinition(new String[]{"L"});

    /**
     * QualifiersDefinition for language and country
     */
    private static final QualifiersDefinition LC_QUALIFIERS = new QualifiersDefinition(new String[]{"L", "C"});

    /**
     * QualifiersDefinition for language,country, variant
     */
    private static final QualifiersDefinition LCV_QUALIFIERS = new QualifiersDefinition(new String[]{"L", "C", "V"});

    private final String[] names;

    public QualifiersDefinition(String[] names) {
        this.names = names;
    }

    public static QualifiersDefinition of(String[] names) {
        return new QualifiersDefinition(names);
    }

    /**
     * QualifiersDefinition for language
     */
    public static QualifiersDefinition ofL() {
        return L_QUALIFIERS;
    }

    /**
     * QualifiersDefinition for language and country
     */
    public static QualifiersDefinition ofLC() {
        return LC_QUALIFIERS;
    }

    /**
     * QualifiersDefinition for language + country + variant
     */
    public static QualifiersDefinition ofLCV() {
        return LCV_QUALIFIERS;
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
