package colesico.framework.security.flow;

abstract public class Task<V> implements FlowNode<V> {

    protected final String id;
    protected Connector<V> connector;

    public Task(String id) {
        this.id = id;
    }

    abstract public void execute();

    public void connect(Connector<V> connector) {
        this.connector = connector;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public void enter(FlowContext<V> ctx) {
        execute();
        if (connector != null) {
            ctx.setCurrentNode(connector.node());
        } else {
            ctx.finish();
        }
    }
}