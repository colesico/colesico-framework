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

import java.util.HashMap;
import java.util.Map;

/**
 * Localization assistant
 */
public final class Localizer {

    private static final String ANY_VALUE = "*";

    /**
     * Tree root node
     */
    private final Node rootNode = new Node();

    public Localizer() {
    }

    /**
     * Adds resource localization
     */
    public void addLocalization(final SubjectQualifiers qualifiers) {
        Node lastNode = provideLastNode(qualifiers);
        lastNode.setQualifiers(qualifiers);
    }

    /**
     * Returns matched resource qualifiers.
     *
     * @param profileQualifiers qualification obtained from profile
     * @return null if no qualifier bound
     */
    public SubjectQualifiers localize(final ProfileQualifiers profileQualifiers) {
        Node curNode = rootNode;
        for (String q : profileQualifiers) {
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

    private Node provideLastNode(SubjectQualifiers subjectQualifiers) {
        Node curNode = rootNode;
        for (String qualifierValue : subjectQualifiers) {
            if (qualifierValue == null) {
                qualifierValue = ANY_VALUE;
            }
            curNode = curNode.provideNext(qualifierValue);
        }
        return curNode;
    }

    private static final class Node {

        // Node subject qualifiers
        private SubjectQualifiers qualifiers;

        // Nested named nodes
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

        public SubjectQualifiers getQualifiers() {
            return qualifiers;
        }

        public void setQualifiers(SubjectQualifiers qualifiers) {
            this.qualifiers = qualifiers;
        }

        @Override
        public String toString() {
            return "Localizer.Node{qualifiers=" + qualifiers + '}';
        }
    }
}
