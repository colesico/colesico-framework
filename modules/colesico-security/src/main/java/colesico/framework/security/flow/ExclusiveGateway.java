package colesico.framework.security.flow;

public class ExclusiveGateway<V> extends AbstractGateway<V> {

    public ExclusiveGateway(String id) {
        super(id);
    }

    @Override
    public void enter(FlowContext<V> ctx) {
        for (var connector : this.connectors) {
            if (connector.canTransit(ctx.value())) {
                ctx.setCurrentNode(connector.node());
                return;
            }
        }
        throw new IllegalStateException("No next condition matched for exclusive gateway " + id());
    }
}
