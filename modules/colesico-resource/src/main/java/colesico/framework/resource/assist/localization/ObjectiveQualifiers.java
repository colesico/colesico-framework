package colesico.framework.resource.assist.localization;

import colesico.framework.resource.ResourceException;

import java.util.Arrays;
import java.util.Iterator;
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

    public static ObjectiveQualifiers of(String... values) {
        return new ObjectiveQualifiers(values);
    }

    public static ObjectiveQualifiers of(QualifiersDefinition definition, Qualifier... qualifiers) {
        if (qualifiers == null || qualifiers.length == 0) {
            throw new ResourceException("Objective qualifiers is empty");
        }
        if (definition == null) {
            throw new ResourceException("Qualifiers definition is null");
        }
        return new ObjectiveQualifiers(definition.toValues(qualifiers));
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
