package colesico.framework.jdbirec.codegen.model;

import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.jdbirec.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static colesico.framework.jdbirec.Record.VIEW_DEFAULT;

public class RecordElement extends ContainerElement {

    /**
     * @see Record#view()
     */
    private final String view;

    public RecordElement(RecordKitElement recordKit, ClassType type, String view) {
        super(recordKit, type);
        this.view = view;
    }

    public List<ColumnElement> getAllColumns() {
        List<ColumnElement> columns = new ArrayList<>();
        collectAllColumns(columns);
        return columns;
    }

    public boolean isDefaultView() {
        return VIEW_DEFAULT.equals(view);
    }

    public String getView() {
        return view;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RecordElement that = (RecordElement) o;
        return Objects.equals(view, that.view);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(view);
    }

    @Override
    public String toString() {
        return "RecordElement{" +
                "originType=" + type +
                ", view='" + view + '\'' +
                '}';
    }
}
