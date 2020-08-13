package colesico.framework.rpc.internal;

import colesico.framework.rpc.teleapi.RpcDataPort;
import colesico.framework.rpc.teleapi.RpcTIContext;
import colesico.framework.rpc.teleapi.RpcTeleDriver;
import colesico.framework.teleapi.MethodInvoker;

import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class RpcTeleDriverImpl implements RpcTeleDriver {

    protected final Provider<RpcDataPort> dataPortProv;

    public RpcTeleDriverImpl(Provider<RpcDataPort> dataPortProv) {
        this.dataPortProv = dataPortProv;
    }

    @Override
    public <T> void invoke(T service, MethodInvoker<T, RpcDataPort> invoker, RpcTIContext invCtx) {
        invoker.invoke(service, dataPortProv.get());
    }

}
