package colesico.framework.dao.codegen.model;

import javax.lang.model.element.VariableElement;

/**
 * Table column bountd to class filed
 */
public class FieldColumnElement extends ColumnElement {

    private final VariableElement originField;

    public FieldColumnElement(VariableElement originField) {
        this.originField = originField;
        this.uid = originField.getSimpleName().toString();
    }

    public VariableElement getOriginField() {
        return originField;
    }
}
