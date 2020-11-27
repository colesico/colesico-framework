package colesico.framework.rpc.teleapi.client;

import colesico.framework.rpc.teleapi.RpcRequest;
import colesico.framework.rpc.teleapi.RpcResponse;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * RPC client base
 */
abstract public class AbstractRpcClient implements RpcClient {

    abstract protected <T> void serialize(T obj, OutputStream os);

    abstract protected <T> T deserialize(InputStream is, Class<T> type);

    abstract protected InputStream invokeServer(String endpoint, byte[] data);

    @Override
    public <R> RpcResponse<R> serve(String apiName, String methodName, RpcRequest request, Class<? extends RpcResponse<R>> responseType) {

        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        serialize(request, os);

        String endpoint = resolveEndpoint(apiName);
        InputStream responseStream = invokeServer(endpoint, os.toByteArray());

        RpcResponse<R> rpcResponse = deserialize(responseStream, responseType);

        return rpcResponse;
    }

    protected String resolveEndpoint(String apiName) {
        return null;
    }

}
