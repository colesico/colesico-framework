package colesico.framework.dao.codegen.model;

import colesico.framework.assist.codegen.CodegenException;

import javax.lang.model.element.TypeElement;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class TableElement {

    private final TypeElement originClass;

    /**
     * Table name
     */
    private String name;
    private final Map<String, ColumnElement> columns = new LinkedHashMap<>();

    private PrimaryKeyElement primaryKey;

    private String comment;

    private Map<String, ForeignKeyElement> foreignKeys = new LinkedHashMap<>();

    public TableElement(TypeElement originClass) {
        this.originClass = originClass;
    }

    public void addColumn(ColumnElement columnElement) {
        if (columns.containsKey(columnElement.getName())) {
            throw CodegenException.of().message("Duplicate table column: " + columnElement.getName()).build();
        }
        columns.put(columnElement.getName(), columnElement);
        columnElement.setParentTable(this);
    }

    public void updateColumn(ColumnElement columnElement) {
        if (!columns.containsKey(columnElement.getName())) {
            throw CodegenException.of().message("Undefined table column: " + columnElement.getName()).build();
        }

        ColumnElement ce = columns.get(columnElement.getName());
        ce.updateWith(columnElement);
        columnElement.setParentTable(this);
    }

    public TypeElement getOriginClass() {
        return originClass;
    }

    public String getName() {
        return name;
    }

    public ColumnElement getColumn(String columnName) {
        return columns.get(columnName);
    }

    public void addForeignKey(ForeignKeyElement fkElm) {
        for (String localColumn : fkElm.getLocalColumns()) {
            if (getColumn(localColumn) == null) {
                throw CodegenException.of().message("Unknown local column '" + localColumn + "' for foreign key: "
                        + fkElm.getName()).element(getOriginClass()).build();
            }
        }
        foreignKeys.put(fkElm.getName(), fkElm);
    }

    public Collection<ForeignKeyElement> getForeignKeys() {
        return foreignKeys.values();
    }

    public Collection<ColumnElement> getColumns() {
        return columns.values();
    }

    public PrimaryKeyElement getPrimaryKey() {
        return primaryKey;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrimaryKey(PrimaryKeyElement primaryKey) {
        for (String column:primaryKey.getColumns()){
            if (getColumn(column)==null){
                throw CodegenException.of().message("Undefined PK column '"+column+"'").element(getOriginClass()).build();
            }
        }
        this.primaryKey = primaryKey;
        primaryKey.setParentTable(this);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
