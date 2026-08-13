package colesico.framework.security.flow;

import java.util.function.Predicate;

public class Connector {

    private final FlowNode node;
    private final Predicate<FlowContext> condition;

    public Connector(FlowNode node) {
        this(node, ctx -> true);
    }

    public Connector(FlowNode node, Predicate<FlowContext> condition) {
        this.node = node;
        this.condition = condition;
    }

    public boolean canTransit(FlowContext ctx) {
        return condition.test(ctx);
    }

    public FlowNode node() {
        return node;
    }
}
