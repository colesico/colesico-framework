package colesico.framework.security.assist.authentication.flow;

public interface FlowNode<V> {
    String id();

    void enter(FlowContext<V> ctx);
}
