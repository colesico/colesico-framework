package colesico.framework.rpc.internal;

import colesico.framework.rpc.RpcError;
import colesico.framework.rpc.clientapi.RpcErrorHandler;

public class DefaultRpcErrorHandler implements RpcErrorHandler {
    @Override
    public Class getSupportedType() {
        // TODO:
        return null;
    }

    @Override
    public RuntimeException createException(RpcError error) {
        // TODO:
        return null;
    }
}
