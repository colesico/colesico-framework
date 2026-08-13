package colesico.framework.security.flow;

public class EndEvent implements FlowNode {
    protected final String id;

    public EndEvent(String id) {
        this.id = id;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public void enter(FlowContext ctx) {
        ctx.finish();
    }
}
