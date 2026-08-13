package colesico.framework.security.flow;

public class ExclusiveGateway extends AbstractGateway {

    public ExclusiveGateway(String id) {
        super(id);
    }

    @Override
    public void enter(FlowContext ctx) {
        for (var connector : this.connectors) {
            if (connector.canTransit(ctx)) {
                ctx.setCurrentNode(connector.node());
                return;
            }
        }
        throw new IllegalStateException("No next condition matched for exclusive gateway " + id());
    }
}
