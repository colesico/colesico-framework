package colesico.framework.service.codegen.model;

import colesico.framework.assist.codegen.model.VarElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents tele method compound parameter
 * @see colesico.framework.service.Compound
 */
public class TeleCompoundElement extends TeleVarElement {

    private final List<TeleVarElement> fields = new ArrayList<>();

    public TeleCompoundElement(VarElement originVariable) {
        super(originVariable);
    }

    public void addField(TeleVarElement field) {
        fields.add(field);
        field.setParentCompound(this);
    }

    public List<TeleVarElement> getFields() {
        return fields;
    }
}
