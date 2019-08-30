package colesico.framework.jdbirec.codegen.model;

import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.assist.codegen.model.ClassType;

import java.util.ArrayList;
import java.util.List;

public class RecordElement {

    private final ClassElement originClass;
    private final CompositionElement rootComposition;
    private final String view;
    private ClassType extend;
    private String tableName;


    public RecordElement(ClassElement originClass, String view) {
        this.originClass = originClass;
        this.rootComposition = new CompositionElement(this, originClass, null);
        this.view = view;
    }

    public CompositionElement getRootComposition() {
        return rootComposition;
    }

    public ClassElement getOriginClass() {
        return originClass;
    }

    public boolean hasColumn(ColumnElement columnElement) {
        return rootComposition.hasColumn(columnElement);
    }

    public List<ColumnElement> getAllColumns() {
        List<ColumnElement> result = new ArrayList<>();
        rootComposition.collectSubColumns(result);
        return result;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public ClassType getExtend() {
        return extend;
    }

    public void setExtend(ClassType extend) {
        this.extend = extend;
    }

    public String getView() {
        return view;
    }
}
