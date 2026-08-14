package colesico.framework.security.assist.authentication.flow;

public class StartEvent<V> implements FlowNode<V> {
    protected final String id;
    protected Connector connector;

    public StartEvent(String id) {
        this.id = id;
    }

    public void connect(Connector connector) {
        this.connector = connector;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public void enter(FlowContext ctx) {
        if (connector != null) {
            ctx.setCurrentNode(connector.node());
        } else {
            ctx.finish();
        }
    }
}
