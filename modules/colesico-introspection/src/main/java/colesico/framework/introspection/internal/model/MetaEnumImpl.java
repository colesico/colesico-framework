package colesico.framework.introspection.internal.model;

import colesico.framework.introspection.MetaEnum;
import colesico.framework.introspection.MetaEnumConstant;

public final class MetaEnumImpl<E> extends MetaInterfaceImpl<E> implements MetaEnum<E> {

    private E enumeration;

    private MetaEnumConstant<E>[] constants;

    @Override
    public E getEnumeration() {
        return enumeration;
    }

    public void setEnumeration(E enumeration) {
        this.enumeration = enumeration;
    }

    @Override
    public MetaEnumConstant<E>[] getConstants() {
        return constants;
    }

    public void setConstants(MetaEnumConstant<E>[] constants) {
        this.constants = constants;
    }
}
