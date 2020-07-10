package colesico.framework.rpc;

/**
 * Basic rpc exception
 */
public final class RpcException extends RuntimeException {
    private final RpcError error;

    public RpcException(RpcError error) {
        super(error.getMessage());
        this.error = error;
    }

    public RpcException(RpcError error, Throwable cause) {
        super(error.getMessage(), cause);
        this.error = error;
    }

    public RpcError getError() {
        return error;
    }
}
