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

public final class Localizer {

    private static final String ANY_VALUE = "*";

    /**
     * Tree root node
     */
    private final Node rootNode = new Node();

    public Localizer() {
    }

    /**
     * Binds qualifiers to subject.
     *
     * @param qualifierNames all possible qualifiers names in predefined order.
     * @param qualifiersSetSpec qualifiers set specification string in the format: qualifier1=value1;qualifier2=value2...
     */
    public void add(final String[] qualifierNames, final String... qualifiersSetSpec) {
        for (String qsItem : qualifiersSetSpec) {
            LinkedHashMap<String, String> qualifiersMap = parseQualifiersSetSpec(qsItem);
            checkNames(qualifierNames, qualifiersMap);
            Node lastNode = provideLastNode(qualifierNames, qualifiersMap);
            lastNode.setQualifiers(toQualifiers(qualifiersMap));
        }
    }

    /**
     * Returns matched qualifiers.
     *
     * @param masterQualifiers must fully be in accordance with qualifierNames in specify(...) method (by number and order)
     * @return null if no qualifier bound
     */
    public String[] localize(final String[] masterQualifiers) {
        Node curNode = rootNode;
        for (String q : masterQualifiers) {
            Node node = curNode.getNext(q);
            if (node == null) {
                node = curNode.getNext(ANY_VALUE);
                if (node == null) {
                    return null;
                }
            }
            curNode = node;
        }
        return curNode.getQualifiers();
    }

    private String[] toQualifiers(LinkedHashMap<String, String> qualifiersMap) {
        String[] result = new String[qualifiersMap.size()];
        int i = 0;
        for (Map.Entry<String, String> q : qualifiersMap.entrySet()) {
            result[i++] = q.getValue();
        }
        return result;
    }

    private Node provideLastNode(String[] qualifierNames, Map<String, String> qualifiersMap) {
        Node curNode = rootNode;
        for (String qualifierName : qualifierNames) {
            String qualifierValue = qualifiersMap.get(qualifierName);
            if (qualifierValue == null) {
                qualifierValue = ANY_VALUE;
            }
            curNode = curNode.provideNext(qualifierValue);
        }
        return curNode;
    }

    /**
     * Returns map of qualifierName=>qualifierValue
     * @param qualifiersSpec
     * @return
     */
    private LinkedHashMap<String, String> parseQualifiersSetSpec(String qualifiersSpec) {
        LinkedHashMap<String, String> result = new LinkedHashMap<>();
        StringTokenizer st = new StringTokenizer(qualifiersSpec, ";");
        while (st.hasMoreElements()) {
            String qualifierSpec = st.nextToken();
            QualifierSpec qualifier = parseQualifierSpec(qualifierSpec);
            result.put(qualifier.getKey(), qualifier.getValue());
        }
        return result;
    }

    private void checkNames(String[] qualifierNames, Map<String, String> qualifiersMap) {
        final Set namesSet = new HashSet();
        namesSet.addAll(Arrays.asList(qualifierNames));

        for (String name : qualifiersMap.keySet()) {
            if (!namesSet.contains(name)) {
                throw new RuntimeException("Invalid qualifier name: " + name);
            }
        }
    }

    private QualifierSpec parseQualifierSpec(String qualifierSpec) {
        StringTokenizer st = new StringTokenizer(qualifierSpec, "=");
        String name = StringUtils.trim(st.nextToken());
        if (!st.hasMoreElements()) {
            throw new RuntimeException("Invalid qualifier specification format: " + qualifierSpec);
        }
        String val = StringUtils.trim(st.nextToken());
        return new QualifierSpec(name, val);
    }

    private static class QualifierSpec {
        private final String key;
        private final String value;

        public QualifierSpec(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

    private static final class Node {
        // node qualification
        private String[] qualifiers;

        // nested named nodes
        private Map<String, Node> nextNodes;

        public Node getNext(String name) {
            return nextNodes == null ? null : nextNodes.get(name);
        }

        public Node provideNext(String name) {
            if (nextNodes == null) {
                nextNodes = new HashMap<>();
            }
            return nextNodes.computeIfAbsent(name, key -> new Node());
        }

        public String[] getQualifiers() {
            return qualifiers;
        }

        public void setQualifiers(String[] qualifiers) {
            this.qualifiers = qualifiers;
        }

        @Override
        public String toString() {
            return "Localizer.Node{qualifiers=" + Arrays.toString(qualifiers) + '}';
        }
    }
}
