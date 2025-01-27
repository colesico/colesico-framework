package colesico.framework.resource.assist.localization;

import colesico.framework.resource.ResourceException;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Localization matcher.
 * Finds the best matched {@link SubjectQualifiers} for
 * given {@link ObjectiveQualifiers} qualifiers
 * <p>
 * The match is finding by lookup the subject qualifier tree.
 */
public final class Matcher<V> {

    /**
     * Represents any qualifier value for null values
     */
    public static final String ANY_VALUE = "*";

    /**
     * {@link SubjectQualifiers} tree root node.
     */
    private final QualifierNode<V> rootNode = new QualifierNode<>();

    /**
     * Register resource qualifiers  ({@link SubjectQualifiers})  in matcher
     */
    public void addQualifiers(final SubjectQualifiers qualifiers, V value) {
        var lastNode = provideLastNode(qualifiers);
        lastNode.setValue(value);
    }

    /**
     * Returns resource {@link SubjectQualifiers} best matched  to {@link ObjectiveQualifiers} qualifiers.
     *
     * @param objectiveQualifiers qualification obtained from profile
     * @return null if no subject qualification matched
     */
    public MatchResult<V> match(final ObjectiveQualifiers objectiveQualifiers) {
        var curNode = rootNode;
        Deque<String> subjectValueQueue = new LinkedList<>();

        for (String objectiveValue : objectiveQualifiers) {

            String subjectValue = objectiveValue;

            if (objectiveValue == null) {
                objectiveValue = ANY_VALUE;
            }

            var node = curNode.getNextNode(objectiveValue);
            // If no exactly node then try any value node
            if (node == null) {
                node = curNode.getNextNode(ANY_VALUE);
                if (node == null) {
                    return null;
                }
                subjectValue = null;
            }

            subjectValueQueue.add(subjectValue);
            curNode = node;

        }

        SubjectQualifiers subjectQualifiers = SubjectQualifiers.of(subjectValueQueue.toArray(String[]::new));
        return new MatchResult<>(subjectQualifiers, curNode.getValue());
    }

    private QualifierNode<V> provideLastNode(SubjectQualifiers qualifiers) {
        var curNode = rootNode;
        for (String qualifierValue : qualifiers) {
            if (qualifierValue == null) {
                qualifierValue = ANY_VALUE;
            }
            curNode = curNode.provideNextNode(qualifierValue);
        }
        return curNode;
    }

    public record MatchResult<V>(SubjectQualifiers subjectQualifiers, V value) {
    }

    private static class QualifierNode<V> {

        // Node value
        protected V value;

        // Direct descendants
        protected Map<String, QualifierNode<V>> descendants;

        protected QualifierNode<V> getNextNode(String name) {
            return descendants == null ? null : descendants.get(name);
        }

        public QualifierNode<V> provideNextNode(String name) {
            if (descendants == null) {
                descendants = new HashMap<>();
            }
            return descendants.computeIfAbsent(name, key -> new QualifierNode<>());
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            if (this.value != null) {
                throw new ResourceException("Qualifier node value already defined: " + value);
            }
            this.value = value;
        }

        @Override
        public String toString() {
            return "Matcher.QualifierNode{value=" + value + '}';
        }
    }


}
