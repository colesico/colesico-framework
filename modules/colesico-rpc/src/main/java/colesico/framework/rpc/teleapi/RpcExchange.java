package colesico.framework.rpc.teleapi;

import colesico.framework.rpc.RpcError;

import java.lang.reflect.Type;

/**
 * Facade for interaction within given transport
 */
public interface RpcExchange {

    /**
     * Returns operation from request
     */
    Operation resolveOperation();

    <Q extends RpcRequest> Q readRequest(Type requestType);

    <R extends RpcResponse> void writeResponse(R response);

    /**
     * Send common error.
     * On client side this error must be represented as an {@link colesico.framework.rpc.RpcException}
     */
    void sendError(RpcError error);

    /**
     * Rpc namespce, api and method name to be invoked
     */
    record Operation(String rpcNamespace, String rpcApiName, String rpcMethodName) {

        @Override
        public String toString() {
            return "Operation{" +
                    "rpcNamespace='" + rpcNamespace + '\'' +
                    ", rpcApiName='" + rpcApiName + '\'' +
                    ", rpcMethodName='" + rpcMethodName + '\'' +
                    '}';
        }
    }
}
