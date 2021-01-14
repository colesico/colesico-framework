package colesico.framework.introspection.internal.model;

import colesico.framework.introspection.MetaField;

public final class MetaFieldImpl<C, T> extends MetaElementImpl implements MetaField<C, T> {

    private Class<T> type;
    private Getter<C, T> getter;
    private Setter<C, T> setter;

    @Override
    public T getValue(C instance) {
        return getter.get(instance);
    }

    @Override
    public void setValue(C instance, T value) {
        setter.set(instance, value);
    }

    @Override
    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    public Getter<C, T> getGetter() {
        return getter;
    }

    public void setGetter(Getter<C, T> getter) {
        this.getter = getter;
    }

    public Setter<C, T> getSetter() {
        return setter;
    }

    public void setSetter(Setter<C, T> setter) {
        this.setter = setter;
    }
}
