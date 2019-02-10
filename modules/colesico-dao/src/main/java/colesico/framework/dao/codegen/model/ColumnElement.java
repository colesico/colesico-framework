package colesico.framework.dao.codegen.model;

import com.squareup.javapoet.TypeName;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class ColumnElement {

    protected final VariableElement originField;
    protected CompositionElement parentComposition;
    protected final String name;
    protected TypeMirror converter;

    public ColumnElement(VariableElement originField, String name) {
        this.originField = originField;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public CompositionElement getParentComposition() {
        return parentComposition;
    }

    public void setParentComposition(CompositionElement parentComposition) {
        this.parentComposition = parentComposition;
    }

    public TypeMirror getConverter() {
        return converter;
    }

    public void setConverter(TypeMirror converter) {
        this.converter = converter;
    }

    public VariableElement getOriginField() {
        return originField;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ColumnElement that = (ColumnElement) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
