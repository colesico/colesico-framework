package colesico.framework.rpc.teleapi;

import java.lang.reflect.Type;

public interface RpcExchange {

    RequestResolution resolveRequest();

    <Q extends RpcRequest> Q readRequest(Type requestType);

    <R extends RpcResponse> void writeResponse(R response);

    /**
     * To write general exception
     */
    void writeException(Throwable t);

    final class RequestResolution {
        private final String apiName;
        private final String methodName;

        public RequestResolution(String apiName, String methodName) {
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
            return "{ apiName='" + apiName + '\'' +
                    "; methodName='" + methodName + '\'' +
                    '}';
        }
    }
}
