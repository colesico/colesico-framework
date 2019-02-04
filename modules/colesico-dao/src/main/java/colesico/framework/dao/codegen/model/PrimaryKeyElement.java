package colesico.framework.dao.codegen.model;

import colesico.framework.assist.codegen.CodegenException;

import java.util.LinkedHashSet;
import java.util.Set;

public class PrimaryKeyElement {

    private TableElement parentTable;
    private String name;
    private final Set<String> columns = new LinkedHashSet<>();

    public void addColumn(String name) {
        if (!columns.add(name)) {
            throw CodegenException.of().message("Duplicate PK column: " + name).build();
        }
    }

    public TableElement getParentTable() {
        return parentTable;
    }

    public void setParentTable(TableElement parentTable) {
        this.parentTable = parentTable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getColumns() {
        return columns;
    }


}
