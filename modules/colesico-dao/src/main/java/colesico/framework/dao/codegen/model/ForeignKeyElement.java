package colesico.framework.dao.codegen.model;

public class ForeignKeyElement {
    private String name;
    private String foreignTable;
    private String[] localColumns;
    private String[] foreignColumns;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getForeignTable() {
        return foreignTable;
    }

    public void setForeignTable(String foreignTable) {
        this.foreignTable = foreignTable;
    }

    public String[] getLocalColumns() {
        return localColumns;
    }

    public void setLocalColumns(String[] localColumns) {
        this.localColumns = localColumns;
    }

    public String[] getForeignColumns() {
        return foreignColumns;
    }

    public void setForeignColumns(String[] foreignColumns) {
        this.foreignColumns = foreignColumns;
    }
}
