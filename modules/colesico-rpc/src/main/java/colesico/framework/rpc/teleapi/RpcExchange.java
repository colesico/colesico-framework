package colesico.framework.rpc.teleapi;

import colesico.framework.rpc.RpcError;
import colesico.framework.rpc.RpcException;

import java.lang.reflect.Type;

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
     * Rpc api and method name to be invoked
     */
    final class Operation {
        private final String apiName;
        private final String methodName;

        public Operation(String apiName, String methodName) {
            this.apiName = apiName;
            this.methodName = methodName;
        }

        public String getApiName() {
            return apiName;
        }

        public String getMethodName() {
            return methodName;
        }

        @Override
        public String toString() {
            return "Operation{" +
                    "apiName='" + apiName + '\'' +
                    ", methodName='" + methodName + '\'' +
                    '}';
        }
    }
}
