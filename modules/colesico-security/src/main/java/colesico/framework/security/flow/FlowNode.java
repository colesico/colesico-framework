package colesico.framework.security.flow;

public interface FlowNode<V> {
    String id();

    void enter(FlowContext<V> ctx);
}
