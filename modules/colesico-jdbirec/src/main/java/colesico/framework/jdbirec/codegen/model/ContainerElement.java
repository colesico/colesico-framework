package colesico.framework.jdbirec.codegen.model;

import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.jdbirec.Composition;
import colesico.framework.jdbirec.RecordView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

abstract public class ContainerElement {

    /**
     * Parent record ref
     */
    protected final RecordElement record;

    /**
     * Container class type
     */
    protected final ClassType type;

    /**
     * Container name
     *
     * @see Composition#name()
     * @see RecordView#name()
     */
    private final String name;

    /**
     * Columns list exported from composition class
     *
     * @see Composition#columnOverriding()
     */
    protected final List<ColumnOverridingElement> columnOverriding = new ArrayList<>();

    /**
     * Container columns
     */
    protected final Set<ColumnElement> columns = new LinkedHashSet<>();

    /**
     * Nested containers
     */
    protected final Set<CompositionElement> compositions = new LinkedHashSet<>();

    /**
     * @see Composition#tagFilter()
     * @see RecordView#tagFilter()
     */
    protected TagFilterElement tagFilter = new TagFilterElement();

    /**
     * @see Composition#renaming()
     * @see RecordView#renaming()
     */
    protected String renaming = "";

    /**
     * Container table name.
     * Can be differ from record table name specified for joint records
     */
    private String tableName;

    public ContainerElement(RecordElement record, ClassType type, String name) {
        this.record = record;
        this.type = type;
        this.name = name;
    }

    /**
     * Checks that the given column contains in this composition or nested.
     * This used to eliminate column duplication
     */
    public boolean hasColumn(ColumnElement columnElement) {
        if (columns.contains(columnElement)) {
            return true;
        }
        for (ContainerElement sce : compositions) {
            if (sce.hasColumn(columnElement)) {
                return true;
            }
        }
        return false;
    }

    public void collectAllColumns(List<ColumnElement> allColumns) {
        allColumns.addAll(columns);
        for (ContainerElement cont : compositions) {
            cont.collectAllColumns(allColumns);
        }
    }

    public void addComposition(CompositionElement composition) {
        compositions.add(composition);
    }

    public void addColumnOverriding(ColumnOverridingElement overriding) {
        columnOverriding.add(overriding);
    }

    public void addColumn(ColumnElement column) {
        columns.add(column);
        column.setContainer(this);
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public ClassType getType() {
        return type;
    }

    public String getTableName() {
        return tableName;
    }

    public TagFilterElement getTagFilter() {
        return tagFilter;
    }

    public void setTagFilter(TagFilterElement tagFilter) {
        this.tagFilter = tagFilter;
    }

    public String getRenaming() {
        return renaming;
    }

    public void setRenaming(String renaming) {
        this.renaming = renaming;
    }

    public List<ColumnOverridingElement> getColumnOverriding() {
        return columnOverriding;
    }

    public Set<ColumnElement> getColumns() {
        return columns;
    }

    public Set<CompositionElement> getCompositions() {
        return compositions;
    }

    public String getName() {
        return name;
    }

    public RecordElement getRecord() {
        return record;
    }
}
