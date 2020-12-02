package colesico.framework.rpc;

public class RpcError {

    private String exceptionType;
    private String message;

    public RpcError() {
    }

    private RpcError(String exceptionType, String message) {
        this.message = message;
    }

    public static RpcError of(String message) {
        return new RpcError(RpcException.class.getCanonicalName(), message);
    }

    public static RpcError of(Class<? extends Throwable> exceptionType) {
        return new RpcError(exceptionType.getCanonicalName(), null);
    }

    public static RpcError of(String exceptionType, String message) {
        return new RpcError(exceptionType, message);
    }

    public static RpcError of(Class<? extends Throwable> exceptionType, String message) {
        return new RpcError(exceptionType.getCanonicalName(), message);
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "RpcError{" +
                "exceptionType='" + exceptionType + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
