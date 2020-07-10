package colesico.framework.rpc.teleapi;

import java.util.Map;

import colesico.framework.rpc.RpcError;

/**
 * RPC response envelope
 */
public final class RpcResponse {

    /**
     * RPC version
     */
    private Integer version;

    /**
     * Request headers
     */
    private Map<String, Object> headers;

    /**
     * Successful result
     */
    private Object result;

    /**
     * Error result
     */
    private RpcError error;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public RpcError getError() {
        return error;
    }

    public void setError(RpcError error) {
        this.error = error;
    }
}
