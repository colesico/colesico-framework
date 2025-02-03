package colesico.framework.resource.l10n;

import colesico.framework.resource.ResourceException;
import colesico.framework.resource.internal.l10n.QualifiersMatcher;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Definition of all possible qualifier names and its canonical order.
 * Order of qualifiers is important due {@link QualifiersMatcher} process.
 * The canonical order of qualifiers essentially determines their priority when determining
 * the best match of subject qualifiers to  objective ones.
 */
public final class QualifiersDefinition implements Iterable<String> {

    /**
     * All possible qualifiers names in canonical order
     */
    private final String[] names;

    public QualifiersDefinition(String[] names) {
        this.names = names;
    }

    public static QualifiersDefinition of(String... names) {
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
     * Return qualifiers values in canonical form  (defined number and order)
     */
    public String[] canonicalize(Map<String, String> qualifiers) {
        String[] values = new String[names.length];
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            String value = qualifiers.get(name);
            if (value != null) {
                values[i] = value;
                qualifiers.remove(name);
            } else {
                values[i] = null;
            }
        }
        if (!qualifiers.isEmpty()) {
            throw new ResourceException("Invalid qualifier name: " + qualifiers.values().iterator().next());
        }
        return values;
    }

    /**
     * Return qualifiers values in canonical form  (defined number and order)
     */
    public String[] canonicalize(Qualifier[] qualifiers) {
        Map<String, String> qmap = new HashMap<>();
        for (Qualifier q : qualifiers) {
            qmap.put(q.name(), q.value());
        }
        return canonicalize(qmap);
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
