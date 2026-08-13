package colesico.framework.security.flow;

public class FlowEngine<V> {
    public void run(FlowContext<V> ctx) {
        while (!ctx.isFinished()) {
            FlowNode<V> node = ctx.currentNode();
            node.enter(ctx);
        }
    }
}
