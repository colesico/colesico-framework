package colesico.framework.rpc.clientapi.handler;

import colesico.framework.rpc.RpcError;
import colesico.framework.rpc.RpcException;
import colesico.framework.rpc.clientapi.RpcErrorHandler;

import javax.inject.Singleton;

@Singleton
public class BasicRpcErrorHandler implements RpcErrorHandler<RpcError> {

    @Override
    public RuntimeException createException(RpcError error) {
        try {
            Class exClass = Class.forName(error.getExceptionType());
            RuntimeException ex = (RuntimeException) exClass
                    .getDeclaredConstructor(String.class)
                    .newInstance(error.getMessage());
            return ex;
        } catch (Exception e) {
            return new RpcException(error.getMessage());
        }
    }
}
