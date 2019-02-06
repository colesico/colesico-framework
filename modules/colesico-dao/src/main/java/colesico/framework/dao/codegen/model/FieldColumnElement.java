package colesico.framework.dao.codegen.model;

import javax.lang.model.element.VariableElement;
import java.util.Deque;

/**
 * Table column bountd to class filed
 */
public class FieldColumnElement extends ColumnElement {

    private final Deque<VariableElement> originField;

    public FieldColumnElement(Deque<VariableElement> originField) {
        this.originField = originField;
        this.uid = originField.peek().getSimpleName().toString();
    }

    public Deque<VariableElement> getOriginField() {
        return originField;
    }
}
