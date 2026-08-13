package colesico.framework.security.flow;

import java.util.ArrayList;
import java.util.List;

abstract public class AbstractGateway implements FlowNode {

    protected final String id;
    protected List<Connector> connectors = new ArrayList<>();

    public AbstractGateway(String id) {
        this.id = id;
    }

    @Override
    public String id() {
        return id;
    }

    public void connect(Connector connector) {
        this.connectors.add(connector);
    }
}
