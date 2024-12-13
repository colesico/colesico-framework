package colesico.framework.resource.assist.localization;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Localizing qualifiers linked with localization subject  (resource, e.t.c.)
 */
public final class SubjectQualifiers implements Iterable<String> {

    /**
     * Qualifier values should be listed according to canonical qualifiers order {@see QualifiersDefinition}.
     * <p>
     * Some values can be null.
     */
    private final String[] values;

    public static SubjectQualifiers fromMap(Map<String, String> subjectQualifiers, QualifiersDefinition definition) {
        // Check empty
        if (subjectQualifiers == null || subjectQualifiers.isEmpty()) {
            throw new RuntimeException("Subject qualifiers is empty");
        }

        // Check names
        Set<String> definedNames = Set.of(definition.getNames());
        for (String name : subjectQualifiers.keySet()) {
            if (!definedNames.contains(name)) {
                throw new RuntimeException("Undefined qualifier name: " + name);
            }
        }

        // Convert to array
        String[] qualifiersArr = new String[definition.getSize()];
        for (int i = 0; i < definition.getSize(); i++) {
            String name = definition.getName(i);
            if (name != null) {
                qualifiersArr[i] = subjectQualifiers.get(name);
            } else {
                qualifiersArr[i] = null;
            }
        }

        return new SubjectQualifiers(qualifiersArr);
    }

    /**
     * From qualifiers spec str
     *
     * @param qualifiersSpec qualifiers string in the format: qualifierName1=value1,qualifierName2=value2...
     * @param definition
     * @return
     */
    public static SubjectQualifiers fromSpec(String qualifiersSpec, QualifiersDefinition definition) {
        return fromMap(parseQualifiersSpec(qualifiersSpec), definition);
    }

    private static Map<String, String> parseQualifiersSpec(String qualifiersSpec) {
        Map<String, String> result = new HashMap<>();
        StringTokenizer st = new StringTokenizer(qualifiersSpec, ";");
        while (st.hasMoreElements()) {
            String qualifierSpec = st.nextToken();
            String[] qualifier = parseQualifierSpec(qualifierSpec);
            result.put(qualifier[0], qualifier[1]);
        }

        return result;
    }

    private static String[] parseQualifierSpec(String qualifierSpec) {
        StringTokenizer st = new StringTokenizer(qualifierSpec, "=");
        String name = StringUtils.trim(st.nextToken());
        if (!st.hasMoreElements()) {
            throw new RuntimeException("Invalid qualifier specification format: " + qualifierSpec);
        }
        String val = StringUtils.trim(st.nextToken());
        return new String[]{name, val};
    }

    public SubjectQualifiers(String[] values) {
        this.values = values;
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
