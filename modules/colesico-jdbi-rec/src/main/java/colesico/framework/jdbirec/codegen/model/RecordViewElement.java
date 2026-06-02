package colesico.framework.jdbirec.codegen.model;

import colesico.framework.assist.codegen.model.ClassType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static colesico.framework.jdbirec.RecordView.VIEW_DEFAULT;

/**
 * @see colesico.framework.jdbirec.RecordView
 */
public class RecordViewElement extends ContainerElement {

    public RecordViewElement(RecordElement record, ClassType type, String name) {
        super(record, type, name);
    }

    public List<ColumnElement> getAllColumns() {
        List<ColumnElement> columns = new ArrayList<>();
        collectAllColumns(columns);
        return columns;
    }

    public boolean isDefaultView() {
        return VIEW_DEFAULT.equals(getName());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RecordViewElement that = (RecordViewElement) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }

    @Override
    public String toString() {
        return "RecordElement{" +
                "originType=" + type +
                ", name='" + getName() + '\'' +
                '}';
    }
}
