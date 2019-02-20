package colesico.framework.dao.codegen.model;

import colesico.framework.assist.codegen.model.ClassElement;

import java.util.ArrayList;
import java.util.List;

public class DTOElement {

    private final ClassElement originClass;
    private final CompositionElement rootComposition;
    private String tableName;

    public DTOElement(ClassElement originClass) {
        this.originClass = originClass;
        this.rootComposition = new CompositionElement(this, originClass, null);
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
}
