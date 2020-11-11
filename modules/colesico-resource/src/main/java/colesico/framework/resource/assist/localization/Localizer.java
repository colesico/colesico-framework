package colesico.framework.resource.assist.localization;

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
     * @param qualifiers qualification obtained from profile
     * @return null if no qualifier bound
     */
    public SubjectQualifiers localize(final ObjectiveQualifiers qualifiers) {
        Node curNode = rootNode;
        for (String q : qualifiers) {
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

    private Node provideLastNode(SubjectQualifiers qualifiers) {
        Node curNode = rootNode;
        for (String qualifierValue : qualifiers) {
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
