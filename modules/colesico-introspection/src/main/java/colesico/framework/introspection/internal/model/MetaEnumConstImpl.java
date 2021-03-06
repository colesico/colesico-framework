package colesico.framework.introspection.internal.model;

import colesico.framework.introspection.MetaEnumConstant;

public final class MetaEnumConstImpl<E> extends MetaElementImpl implements MetaEnumConstant<E> {

    private Getter<E> getter;

    @Override
    public E getValue() {
        return getter.get();
    }

    public Getter<E> getGetter() {
        return getter;
    }

    public void setGetter(Getter<E> getter) {
        this.getter = getter;
    }

}
