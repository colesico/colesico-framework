package colesico.framework.rpc.clientapi;

import colesico.framework.rpc.RpcError;

public interface RpcErrorHandler<E extends RpcError> {

    /**
     * Supported rpc error type
     */
    Class<E> getSupportedType();

    /**
     * Create exception from error
     */
    RuntimeException createException(E error);
}
