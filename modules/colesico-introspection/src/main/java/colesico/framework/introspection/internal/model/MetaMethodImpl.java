package colesico.framework.introspection.internal.model;

import colesico.framework.introspection.MetaMethod;
import colesico.framework.introspection.MetaParameter;

public final class MetaMethodImpl<C, R> extends MetaElementImpl implements MetaMethod<C, R> {

    private Class<R> returnType;

    private MetaParameter[] parameters;

    private Invoker<C, R> invoker;

    @Override
    public R invoke(C instance, Object... args) {
        return invoker.invoke(instance, args);
    }

    @Override
    public Class<R> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<R> returnType) {
        this.returnType = returnType;
    }

    @Override
    public MetaParameter[] getParameters() {
        return parameters;
    }

    public void setParameters(MetaParameter[] parameters) {
        this.parameters = parameters;
    }

    public Invoker<C, R> getInvoker() {
        return invoker;
    }

    public void setInvoker(Invoker<C, R> invoker) {
        this.invoker = invoker;
    }
}
