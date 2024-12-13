package colesico.framework.resource.assist.localization;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Defines all possible qualifier names and its canonical order.
 * Order of qualifiers is important due localization process that defines look up
 * order of localization.
 */
public final class QualifiersDefinition implements Iterable<String> {

    /**
     * QualifiersDefinition for language and country
     */
    public static final QualifiersDefinition L_QUALIFIERS = new QualifiersDefinition(new String[]{"L"});

    /**
     * QualifiersDefinition for language and country
     */
    public static final QualifiersDefinition LC_QUALIFIERS = new QualifiersDefinition(new String[]{"L", "C"});

    /**
     * QualifiersDefinition for language,country, variant
     */
    public static final QualifiersDefinition LCV_QUALIFIERS = new QualifiersDefinition(new String[]{"L", "C", "V"});


    private final String[] names;

    public QualifiersDefinition(String[] names) {
        this.names = names;
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
