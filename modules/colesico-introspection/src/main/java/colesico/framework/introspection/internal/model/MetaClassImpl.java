package colesico.framework.introspection.internal.model;

import colesico.framework.introspection.MetaClass;
import colesico.framework.introspection.MetaConstructor;

public final class MetaClassImpl<C> extends MetaInterfaceImpl<C> implements MetaClass<C> {

    private MetaConstructor<C>[] constructors;

    @Override
    public MetaConstructor<C>[] getConstructors() {
        return constructors;
    }

    public void setConstructors(MetaConstructor<C>[] constructors) {
        this.constructors = constructors;
    }
}
