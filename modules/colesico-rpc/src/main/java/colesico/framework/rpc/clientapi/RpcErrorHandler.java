package colesico.framework.rpc.clientapi;

import colesico.framework.rpc.RpcError;

public interface RpcErrorHandler<E extends RpcError> {

    /**
     * Create exception from error
     */
    RuntimeException createException(E error);
}
