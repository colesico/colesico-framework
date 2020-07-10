package colesico.framework.rpc.teleapi;

import java.util.Map;

/**
 * Rpc request envelope
 */
public final class RpcRequest {

    /**
     * RPC version
     */
    private Integer version;

    /**
     * Request headers
     */
    private Map<String, Object> headers;

    /**
     * Requested class name
     */
    private String targetClass;

    /**
     * Requested method name
     */
    private String targetMethod;

    /**
     * Method parameters
     */
    private Map<String, Object> params;

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

    public String getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }

    public String getTargetMethod() {
        return targetMethod;
    }

    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
