package colesico.framework.service.codegen.model;

import colesico.framework.assist.codegen.model.VarElement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TeleCompoundElement extends TeleArgumentElement {

    private final List<TeleArgumentElement> fields = new ArrayList<>();

    public TeleCompoundElement(VarElement originVariable) {
        super(originVariable);
    }

    public void addField(TeleArgumentElement field) {
        fields.add(field);
        field.setParentCompound(this);
    }

    public List<TeleArgumentElement> getFields() {
        return fields;
    }
}
