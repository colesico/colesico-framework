package colesico.framework.dao.codegen.model;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.assist.codegen.model.FieldElement;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CompositionElement {

    private final DTOElement parentDTO;
    private final ClassElement originType;
    private final FieldElement originalField;
    private CompositionElement parentComposition;
    private String[] acceptFields;
    private String[] renameColumns;

    private final Set<ColumnElement> columns = new LinkedHashSet<>();
    private final Set<CompositionElement> subCompositions = new LinkedHashSet<>();

    public CompositionElement(DTOElement parentDTO, ClassElement originType, FieldElement originalField) {
        this.parentDTO = parentDTO;
        this.originType = originType;
        this.originalField = originalField;
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

    public void collectSubColumns(List<ColumnElement>allSubColumns){
        allSubColumns.addAll(columns);
        for (CompositionElement comp:subCompositions){
            comp.collectSubColumns(allSubColumns);
        }
    }

    public void addSubComposition(CompositionElement compElm) {
        compElm.setParentComposition(this);
        subCompositions.add(compElm);
    }

    public void addColumn(ColumnElement columnElm) {
        if (parentDTO.hasColumn(columnElm)) {
            throw CodegenException.of().message("Duplicate column: " + columnElm.getName()).element(columnElm.getOriginField()).build();
        }
        columns.add(columnElm);
        columnElm.setParentComposition(this);
    }

    public CompositionElement getParentComposition() {
        return parentComposition;
    }

    public Set<ColumnElement> getColumns() {
        return columns;
    }

    public void setParentComposition(CompositionElement parentComposition) {
        this.parentComposition = parentComposition;
    }

    public ClassElement getOriginType() {
        return originType;
    }

    public Set<CompositionElement> getSubCompositions() {
        return subCompositions;
    }

    public FieldElement getOriginalField() {
        return originalField;
    }

    public String[] getAcceptFields() {
        return acceptFields;
    }

    public void setAcceptFields(String[] acceptFields) {
        this.acceptFields = acceptFields;
    }

    public String[] getRenameColumns() {
        return renameColumns;
    }

    public void setRenameColumns(String[] renameColumns) {
        this.renameColumns = renameColumns;
    }

    @Override
    public String toString() {
        return "CompositionElement{" +
                "originType=" + originType +
                ", originalField=" + originalField +
                ", parentComposition=" + parentComposition +
                '}';
    }
}
