package colesico.framework.rpc;

/**
 * Basic remote exception
 */
public final class RpcException extends RuntimeException {
    private final Error error;

    public RpcException(Error error) {
        super(error.getMessage());
        this.error = error;
    }

    public RpcException(Error error, Throwable cause) {
        super(error.getMessage(), cause);
        this.error = error;
    }

    public Error getError() {
        return error;
    }
}
