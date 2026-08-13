package colesico.framework.security.flow;

import java.util.HashMap;
import java.util.Map;

public class FlowContext {

    private FlowNode currentNode;
    private final Map<String, Object> variables = new HashMap<>();

    public void setCurrentNode(FlowNode element) {
        this.currentNode = element;
    }

    public FlowNode currentNode() {
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

    public Map<String, Object> variables() {
        return variables;
    }

    public void setVariable(String name, Object value) {
        variables.put(name, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T variable(String name) {
        return (T) variables.get(name);
    }
}
