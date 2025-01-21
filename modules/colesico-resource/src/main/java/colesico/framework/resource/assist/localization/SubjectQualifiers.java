package colesico.framework.resource.assist.localization;

import colesico.framework.resource.ResourceException;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Localizing qualifiers linked with localization subject  (any resource, etc.)
 * Number of qualifiers and order must be strongly  according
 * to canonical qualifiers number and order defined in {@link QualifiersDefinition}
 * Any undetermined qualifier values must be null.
 */
public final class SubjectQualifiers implements Iterable<String> {

    /**
     * Qualifier values should be listed according to canonical qualifiers order {@see QualifiersDefinition}.
     * <p>
     * Some values can be null.
     */
    private final String[] values;

    public SubjectQualifiers(String[] values) {
        this.values = values;
    }

    public static SubjectQualifiers of(String... values) {
        return new SubjectQualifiers(values);
    }

    public static SubjectQualifiers of(QualifiersDefinition definition, Qualifier... qualifiers) {
        if (qualifiers == null || qualifiers.length == 0) {
            throw new ResourceException("Subject qualifiers is empty");
        }
        if (definition == null) {
            throw new ResourceException("Qualifiers definition is null");
        }
        return new SubjectQualifiers(definition.toValues(qualifiers));
    }

    public String[] getValues() {
        return values;
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
