package colesico.framework.introspection.internal.model;

import colesico.framework.introspection.MetaParameter;

public final class MetaParameterImpl<T> extends MetaElementImpl implements MetaParameter<T> {

    private Class<T> type;

    @Override
    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }
}
