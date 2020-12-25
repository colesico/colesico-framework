package colesico.framework.rpc.clientapi;

/**
 * RPC request handler called before request been sent.
 * Can be used to enrich request with custom values  (security principal, profile, tracing id, e.t.c.)
 */
public interface RpcRequestHandler<Q> {
    void onRequest(Q request);
}
