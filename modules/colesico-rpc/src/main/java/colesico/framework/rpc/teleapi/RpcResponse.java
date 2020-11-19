package colesico.framework.rpc.teleapi;

import colesico.framework.rpc.RpcError;

/**
 * RPC response basis
 */
abstract public class RpcResponse {
    private RpcError error;

    public RpcError getError() {
        return error;
    }

    public void setError(RpcError error) {
        this.error = error;
    }
}
