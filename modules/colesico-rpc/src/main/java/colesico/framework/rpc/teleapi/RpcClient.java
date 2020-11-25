package colesico.framework.rpc.teleapi;

public interface RpcClient {

    String REQUEST_PARAM = "request";
    String CALL_METHOD = "call";

    <R> RpcResponse<R> call(RpcRequest request);
}
