package colesico.framework.rpc.kryo;

import colesico.framework.rpc.client.RpcClient;
import colesico.framework.rpc.teleapi.RpcRequest;
import colesico.framework.rpc.teleapi.RpcResponse;

public class KryoClient implements RpcClient {

    public static final String RPC_API_HEADER = "X-RPC-API";
    public static final String RPC_METHOD_HEADER = "X-RPC-Method";
    public static final String RPC_ERROR_HEADER = "X-RPC-Error";

    private final KryoSerializer serializer;

    public KryoClient(KryoSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public <R> RpcResponse<R> serve(String apiName, String methodName, RpcRequest request, Class<? extends RpcResponse<R>> responseType) {
        return null;
    }
}
