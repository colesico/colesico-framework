package colesico.framework.rpc.teleapi;

public interface RpcExchange {
    RpcRequest readRequest();

    void writeResponse(RpcResponse response);
}
