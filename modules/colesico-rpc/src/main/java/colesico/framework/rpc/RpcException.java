package colesico.framework.rpc;

/**
 * Basic remote exception
 */
public class RpcException extends RuntimeException{
   private final Fault fault;

    public RpcException(Fault fault) {
        this.fault = fault;
    }

    public RpcException(String message, Fault fault) {
        super(message);
        this.fault = fault;
    }

    public RpcException(String message, Throwable cause, Fault fault) {
        super(message, cause);
        this.fault = fault;
    }

    public RpcException(Throwable cause, Fault fault) {
        super(cause);
        this.fault = fault;
    }

    public RpcException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Fault fault) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.fault = fault;
    }

    public Fault getFault() {
        return fault;
    }
}
