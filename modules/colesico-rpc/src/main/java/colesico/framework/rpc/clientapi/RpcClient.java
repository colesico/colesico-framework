package colesico.framework.rpc.clientapi;

import colesico.framework.rpc.teleapi.RpcRequest;
import colesico.framework.rpc.teleapi.RpcResponse;

/**
 * RPC client API used in autogenerated RPC clients to call remote services
 */
public interface RpcClient {

    String REQUEST_PARAM = "request";
    String CALL_METHOD = "call";

    /**
     * Adds extra parameters and call target
     */
    <R> RpcResponse<R> call(String rpcNamespace, String rpcApiName, String rpcMethodName, RpcRequest request, Class<? extends RpcResponse<R>> responseType);
}
