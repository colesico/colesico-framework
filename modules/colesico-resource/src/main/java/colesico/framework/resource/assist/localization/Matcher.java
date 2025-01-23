package colesico.framework.resource.assist.localization;

import java.util.HashMap;
import java.util.Map;

/**
 * Localization assistant.
 * Finds the best matched {@link SubjectQualifiers} for
 * given {@link ObjectiveQualifiers} qualifiers
 * <p>
 * The match is finding by lookup the subject qualifier tree.
 */
public final class Matcher {

    /**
     * Represents any qualifier value for null values
     */
    public static final String ANY_VALUE = "*";

    /**
     * {@link SubjectQualifiers} tree root node.
     */
    private final Node rootNode = new Node();

    /**
     * Register resource qualifiers  ({@link SubjectQualifiers})  in matcher
     */
    public void addQualifiers(final SubjectQualifiers qualifiers) {
        Node lastNode = provideLastNode(qualifiers);
        lastNode.setQualifiers(qualifiers);
    }

    /**
     * Returns resource {@link SubjectQualifiers} best matched  to {@link ObjectiveQualifiers} qualifiers.
     *
     * @param qualifiers qualification obtained from profile
     * @return null if no qualifier bound
     */
    public SubjectQualifiers match(final ObjectiveQualifiers qualifiers) {
        Node curNode = rootNode;
        for (String qualifierValue : qualifiers) {
            if (qualifierValue == null) {
                qualifierValue = ANY_VALUE;
            }
            Node node = curNode.getNext(qualifierValue);
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
            return "Matcher.Node{qualifiers=" + qualifiers + '}';
        }
    }
}
