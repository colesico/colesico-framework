package colesico.framework.rpc.clientapi;

/**
 * RPC response handler called after response received.
 * Can be used to transfer response custom values  (security principal, profile, tracing id, e.t.c.) to current context
 */
public interface RpcResponseHandler<R> {
    void onResponse(R response);
}
