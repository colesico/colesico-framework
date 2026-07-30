package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.service.codegen.model.ServiceParameterElement;

/**
 * Represent  parameter aggregation class and corresponding method parameter
 */
public class TeleAggregateParamElement extends TeleParameterElement {

    private final TeleAggregateElement aggregate;

    public TeleAggregateParamElement(TeleCommandElement parentTeleCommand, ServiceParameterElement serviceParameter, TeleAggregateElement aggregate) {
        super(parentTeleCommand, serviceParameter);
        this.aggregate = aggregate;
    }

    public TeleAggregateElement aggregate() {
        return aggregate;
    }
}
