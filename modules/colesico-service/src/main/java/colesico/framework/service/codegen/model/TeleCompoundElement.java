package colesico.framework.service.codegen.model;

import colesico.framework.assist.codegen.model.VarElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents tele method compound parameter
 * @see colesico.framework.service.Compound
 */
public class TeleCompoundElement extends TeleInputElement {

    private final List<TeleInputElement> fields = new ArrayList<>();

    public TeleCompoundElement(VarElement originVariable) {
        super(originVariable);
    }

    public void addField(TeleInputElement field) {
        fields.add(field);
        field.setParentCompound(this);
    }

    public List<TeleInputElement> getFields() {
        return fields;
    }
}
