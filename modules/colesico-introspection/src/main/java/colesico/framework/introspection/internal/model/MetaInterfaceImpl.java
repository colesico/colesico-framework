package colesico.framework.introspection.internal.model;

import colesico.framework.introspection.MetaField;
import colesico.framework.introspection.MetaInterface;
import colesico.framework.introspection.MetaMethod;

public class MetaInterfaceImpl<C> extends MetaElementImpl implements MetaInterface<C> {

    private Class<C> type;

    private MetaField<C, ?>[] fields;

    private MetaMethod<C, ?>[] methods;

    @Override
    public Class<C> getType() {
        return type;
    }

    public void setType(Class<C> type) {
        this.type = type;
    }

    @Override
    public MetaField<C, ?>[] getFields() {
        return fields;
    }

    public void setFields(MetaField<C, ?>[] fields) {
        this.fields = fields;
    }

    @Override
    public MetaMethod<C, ?>[] getMethods() {
        return methods;
    }

    public void setMethods(MetaMethod<C, ?>[] methods) {
        this.methods = methods;
    }
}
