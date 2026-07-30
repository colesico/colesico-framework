package colesico.framework.service.codegen.model.teleapi;

import colesico.framework.assist.codegen.model.VarElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent  parameter aggregations
 */
public class TeleAggregateElement extends TeleParameterElement {

    private final List<TeleParameterElement> fields = new ArrayList<>();

    public TeleAggregateElement(TeleCommandElement parentTeleCommand, VarElement originParameter) {
        super(parentTeleCommand, originParameter);
    }

    public void addField(TeleParameterElement field) {
        fields.add(field);
    }

    public List<TeleParameterElement> fields() {
        return fields;
    }
}
