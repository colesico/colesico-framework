package colesico.framework.security.flow;

public class FlowContext<V> {

    private FlowNode<V> currentNode;
    private final V value;

    public FlowContext(V value) {
        this.value = value;
    }

    public void setCurrentNode(FlowNode<V> node) {
        this.currentNode = node;
    }

    public FlowNode<V> currentNode() {
        if (currentNode == null) {
            throw new IllegalStateException("Process is not running");
        }
        return currentNode;
    }

    public void finish() {
        this.currentNode = null;
    }

    public boolean isFinished() {
        return currentNode == null;
    }

    public V value() {
        return value;
    }
}
