package colesico.framework.introspection.internal.model;

import colesico.framework.introspection.Kind;
import colesico.framework.introspection.MetaAnnotation;
import colesico.framework.introspection.MetaElement;
import colesico.framework.introspection.Modifier;

abstract public class MetaElementImpl implements MetaElement {

    protected Kind kind;
    protected MetaElement container;
    protected String name;
    protected Modifier[] modifiers;
    protected MetaAnnotation[] annotations;

    @Override
    public Kind getKind() {
        return kind;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    @Override
    public MetaElement getContainer() {
        return container;
    }

    public void setContainer(MetaElement container) {
        this.container = container;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Modifier[] getModifiers() {
        return modifiers;
    }

    public void setModifiers(Modifier[] modifiers) {
        this.modifiers = modifiers;
    }

    @Override
    public MetaAnnotation[] getAnnotations() {
        return annotations;
    }

    public void setAnnotations(MetaAnnotation[] annotations) {
        this.annotations = annotations;
    }
}
