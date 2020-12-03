package colesico.framework.rpc.clientapi;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.key.ClassedKey;
import colesico.framework.rpc.RpcError;

import javax.inject.Singleton;

@Singleton
public class RpcErrorHandlerFactory {

    private final Ioc ioc;

    public RpcErrorHandlerFactory(Ioc ioc) {
        this.ioc = ioc;
    }

    public <E extends RpcError> RpcErrorHandler<E> getErrorHandler(Class<E> error) {
        return ioc.instance(new ClassedKey<>(RpcErrorHandler.class.getCanonicalName(), error.getCanonicalName()), null);
    }
}
