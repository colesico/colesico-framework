package colesico.framework.security.flow;

public class EndEvent<V> implements FlowNode<V> {

    protected final String id;

    public EndEvent(String id) {
        this.id = id;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public void enter(FlowContext<V> ctx) {
        ctx.finish();
    }
}
