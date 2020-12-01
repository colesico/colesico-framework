package colesico.framework.rpc.teleapi.client;

/**
 * RPC request handler called before request been sent.
 * Can be used to enrich request with custom values  (security principal, profile, tracing id, e.t.c.)
 */
public interface RpcRequestHandler<X> {
    void onRequest(X request);
}
