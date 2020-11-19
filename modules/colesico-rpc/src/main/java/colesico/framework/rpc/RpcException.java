package colesico.framework.rpc;

/**
 * Basic rpc exception
 */
public final class RpcException extends RuntimeException {

    public RpcException(String message) {
        super(message);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }
}
