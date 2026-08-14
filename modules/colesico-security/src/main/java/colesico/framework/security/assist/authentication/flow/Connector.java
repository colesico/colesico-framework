package colesico.framework.security.assist.authentication.flow;

import java.util.function.Predicate;

public class Connector<V> {

    private final FlowNode<V> node;
    private final Predicate<V> condition;

    public Connector(FlowNode<V> node) {
        this(node, ctx -> true);
    }

    public Connector(FlowNode<V> node, Predicate<V> condition) {
        this.node = node;
        this.condition = condition;
    }

    public boolean canTransit(V value) {
        return condition.test(value);
    }

    public FlowNode<V> node() {
        return node;
    }
}
