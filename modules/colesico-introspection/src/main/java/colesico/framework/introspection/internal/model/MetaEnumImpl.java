package colesico.framework.introspection.internal.model;

import colesico.framework.introspection.MetaEnum;
import colesico.framework.introspection.MetaEnumConstant;

public final class MetaEnumImpl<E> extends MetaInterfaceImpl<E> implements MetaEnum<E> {

    private MetaEnumConstant<E>[] constants;

    @Override
    public MetaEnumConstant<E>[] getConstants() {
        return constants;
    }

    public void setConstants(MetaEnumConstant<E>[] constants) {
        this.constants = constants;
    }
}
