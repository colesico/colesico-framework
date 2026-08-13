package colesico.framework.security.flow;

public interface FlowNode {
    String id();
    void enter(FlowContext ctx);
}
