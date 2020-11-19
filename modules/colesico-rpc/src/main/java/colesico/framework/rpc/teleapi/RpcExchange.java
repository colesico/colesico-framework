package colesico.framework.rpc.teleapi;

import java.lang.reflect.Type;

public interface RpcExchange {

    <Q extends RpcRequest> Q readRequest(Type requestType);

    <R extends RpcResponse> void writeResponse(R response);

}
