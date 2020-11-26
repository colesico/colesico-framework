package colesico.framework.rpc;

public class RpcError {
    /**
     * Exception class name
     */
    private String exceptionType;
    private String message;

    public RpcError() {
    }

    private RpcError(String exceptionType, String message) {
        this.exceptionType = exceptionType;
        this.message = message;
    }

    public static RpcError of(String exceptionType, String message) {
        return new RpcError(exceptionType, message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }
}
