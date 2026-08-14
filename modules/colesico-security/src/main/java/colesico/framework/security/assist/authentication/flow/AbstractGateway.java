package colesico.framework.security.assist.authentication.flow;

import java.util.ArrayList;
import java.util.List;

abstract public class AbstractGateway<V> implements FlowNode<V> {

    protected final String id;
    protected List<Connector<V>> connectors = new ArrayList<>();

    public AbstractGateway(String id) {
        this.id = id;
    }

    @Override
    public String id() {
        return id;
    }

    public void connect(Connector<V> connector) {
        this.connectors.add(connector);
    }
}
