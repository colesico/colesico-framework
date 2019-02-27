package colesico.framework.jdbirec.codegen.model;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.assist.codegen.model.FieldElement;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CompositionElement {

    private final RecordElement parentRECORD;

    /**
     * Composition class
     */
    private final ClassElement originClass;

    /**
     * Composition field in the parent composition
     */
    private final FieldElement originField;

    private CompositionElement parentComposition;

    private String[] columnsFilter;

    private final Set<ColumnElement> columns = new LinkedHashSet<>();
    private final Set<CompositionElement> subCompositions = new LinkedHashSet<>();

    public CompositionElement(RecordElement parentRECORD, ClassElement originClass, FieldElement originField) {
        this.parentRECORD = parentRECORD;
        this.originClass = originClass;
        this.originField = originField;
    }

    public boolean hasColumn(ColumnElement columnElement) {
        if (columns.contains(columnElement)) {
            return true;
        }
        for (CompositionElement sce : subCompositions) {
            if (sce.hasColumn(columnElement)) {
                return true;
            }
        }
        return false;
    }

    public void collectSubColumns(List<ColumnElement> allSubColumns) {
        allSubColumns.addAll(columns);
        for (CompositionElement comp : subCompositions) {
            comp.collectSubColumns(allSubColumns);
        }
    }

    public void addSubComposition(CompositionElement compElm) {
        compElm.setParentComposition(this);
        subCompositions.add(compElm);
    }

    public void addColumn(ColumnElement columnElm) {
        if (parentRECORD.hasColumn(columnElm)) {
            throw CodegenException.of().message("Duplicate column: " + columnElm.getName()).element(columnElm.getOriginField()).build();
        }
        columns.add(columnElm);
        columnElm.setParentComposition(this);
    }

    public RecordElement getParentRECORD() {
        return parentRECORD;
    }

    public ClassElement getOriginClass() {
        return originClass;
    }

    public FieldElement getOriginField() {
        return originField;
    }

    public CompositionElement getParentComposition() {
        return parentComposition;
    }

    public void setParentComposition(CompositionElement parentComposition) {
        this.parentComposition = parentComposition;
    }

    public String[] getColumnsFilter() {
        return columnsFilter;
    }

    public void setColumnsFilter(String[] columnsFilter) {
        this.columnsFilter = columnsFilter;
    }

    public Set<ColumnElement> getColumns() {
        return columns;
    }

    public Set<CompositionElement> getSubCompositions() {
        return subCompositions;
    }

    @Override
    public String toString() {
        return "CompositionElement{" +
                "originClass=" + originClass +
                ", originField=" + originField +
                '}';
    }
}
