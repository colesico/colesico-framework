package colesico.framework.security.flow;

abstract public class Task implements FlowNode {

    protected final String id;
    protected Connector connector;

    public Task(String id) {
        this.id = id;
    }

    abstract public void execute();

    public void connect(Connector connector) {
        this.connector = connector;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public void enter(FlowContext ctx) {
        execute();
        if (connector != null) {
            ctx.setCurrentNode(connector.node());
        } else {
            ctx.finish();
        }
    }
}