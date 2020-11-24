package colesico.framework.rpc.teleapi;

import colesico.framework.rpc.RpcError;

/**
 * RPC response basis
 */
abstract public class RpcResponse<R> {

    protected RpcError error;

    protected R result;

    public RpcError getError() {
        return error;
    }

    public void setError(RpcError error) {
        this.error = error;
    }

    public R getResult() {
        return result;
    }

    public void setResult(R result) {
        this.result = result;
    }
}
