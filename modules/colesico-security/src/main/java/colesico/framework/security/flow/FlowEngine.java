package colesico.framework.security.flow;

public class FlowEngine {
    public void run(FlowContext ctx) {
        while (!ctx.isFinished()) {
            FlowNode node = ctx.currentNode();
            node.enter(ctx);
        }
    }
}
