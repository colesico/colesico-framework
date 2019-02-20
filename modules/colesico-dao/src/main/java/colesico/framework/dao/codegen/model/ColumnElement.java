package colesico.framework.dao.codegen.model;

import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.assist.codegen.model.FieldElement;

public class ColumnElement {

    protected final FieldElement originField;
    protected CompositionElement parentComposition;
    protected final String name;
    protected ClassType converter;
    protected String formula;
    protected String definition;
    protected boolean option;

    public ColumnElement(FieldElement originField, String name) {
        if (name == null) {
            throw new RuntimeException("Name is null");
        }
        this.name = name;
        this.originField = originField;
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

    public String getName() {
        return name;
    }

    public ClassType getConverter() {
        return converter;
    }

    public void setConverter(ClassType converter) {
        this.converter = converter;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public boolean isOption() {
        return option;
    }

    public void setOption(boolean option) {
        this.option = option;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ColumnElement that = (ColumnElement) o;

        return name.equals(that.name);
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
