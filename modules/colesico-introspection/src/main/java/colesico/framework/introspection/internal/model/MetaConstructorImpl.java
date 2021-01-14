package colesico.framework.introspection.internal.model;

import colesico.framework.introspection.MetaConstructor;
import colesico.framework.introspection.MetaParameter;

public final class MetaConstructorImpl<C> extends MetaElementImpl implements MetaConstructor<C> {

    private MetaParameter[] parameters;

    private Instantiator<C> instantiator;

    @Override
    public C instantiate(Object... args) {
        return instantiator.instantiate(args);
    }

    @Override
    public MetaParameter[] getParameters() {
        return parameters;
    }

    public void setParameters(MetaParameter[] parameters) {
        this.parameters = parameters;
    }

    public Instantiator<C> getInstantiator() {
        return instantiator;
    }

    public void setInstantiator(Instantiator<C> instantiator) {
        this.instantiator = instantiator;
    }
}
