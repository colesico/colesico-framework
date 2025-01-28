package colesico.framework.resource.assist.localization;

import colesico.framework.resource.ResourceException;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Localizing qualifiers linked with localization subject  (any resource, file, etc.)
 * Number of qualifiers and order must be strongly  according
 * to  canonical qualifiers number and order defined in {@link QualifiersDefinition}
 * Some qualifiers can have null values, that is interpreted as any/undetermined value.
 */
public final class SubjectQualifiers implements Iterable<String> {

    /**
     * Qualifier values should be listed according to  canonical qualifiers order
     * {@see QualifiersDefinition}.
     * <p>
     * Some values can be null.
     */
    private final String[] values;

    private SubjectQualifiers(String[] values) {
        this.values = values;
    }

    protected static SubjectQualifiers of(String[] values) {
        return new SubjectQualifiers(values);
    }

    /**
     * Default subject qualifiers
     */
    public static SubjectQualifiers of(QualifiersDefinition definition) {
        String[] values = new String[definition.getSize()];
        Arrays.fill(values, null);
        return new SubjectQualifiers(values);
    }

    public static SubjectQualifiers of(QualifiersDefinition definition, Qualifier... qualifiers) {
        if (qualifiers == null || qualifiers.length == 0) {
            throw new ResourceException("Subject qualifiers is empty");
        }
        if (definition == null) {
            throw new ResourceException("Qualifiers definition is null");
        }
        return new SubjectQualifiers(definition.canonicalize(qualifiers));
    }

    public String[] getValues() {
        return values;
    }

    public List<String> getNotNullValues() {
        List<String> result = new ArrayList<>();

        for (String val : values) {
            if (val != null) {
                result.add(val);
            }
        }

        return result;
    }

    public String toSuffix() {
        List<String> vals = getNotNullValues();
        if (vals.isEmpty()) {
            return "";
        } else {
            return "_" + StringUtils.join(vals, "_");
        }
    }

    @Override
    public Iterator<String> iterator() {
        return Arrays.stream(values).iterator();
    }

    public String toSuffix(char separator) {
        StringBuilder sb = new StringBuilder();
        for (String value : values) {
            if (value != null) {
                sb.append(separator).append(value);
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "SubjectQualifiers{" +
                "values=" + Arrays.toString(values) +
                '}';
    }
}
