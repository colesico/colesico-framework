/*
 * Copyright © 2014-2020 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.profile;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Consumer;

/**
 * Localizing qualifiers linked with localization subject  (resource, e.t.c.)
 */
public final class SubjectQualifiers implements Iterable<String> {

    /**
     * Qualifier values should be listed according to canonical qualifiers order.
     * Some values can be null.
     */
    private final String[] values;

    public static SubjectQualifiers fromMap(Map<String, String> qualifiersMap, QualifierStandard standard) {
        // Check empty
        if (qualifiersMap == null || qualifiersMap.isEmpty()) {
            throw new RuntimeException("Qualifiers is empty");
        }

        // Check names
        Set<String> canonicityNames = Set.of(standard.getNames());
        for (String name : qualifiersMap.keySet()) {
            if (!canonicityNames.contains(name)) {
                throw new RuntimeException("Undefined qualifier name: " + name);
            }
        }

        // Convert to array
        String[] qualifiersArr = new String[standard.getSize()];
        for (int i = 0; i < standard.getSize(); i++) {
            String name = standard.getName(i);
            if (name != null) {
                qualifiersArr[i] = qualifiersMap.get(name);
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
     * @param standard
     * @return
     */
    public static SubjectQualifiers fromSpec(String qualifiersSpec, QualifierStandard standard) {
        return fromMap(parseQualifiersSpec(qualifiersSpec), standard);
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

    @Override
    public void forEach(Consumer<? super String> action) {
        for (String q : values) {
            action.accept(q);
        }
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
        return "ResourceQualifiers{" +
                "values=" + Arrays.toString(values) +
                '}';
    }
}
