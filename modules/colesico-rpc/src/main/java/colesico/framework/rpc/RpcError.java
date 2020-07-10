package colesico.framework.rpc;

public class RpcError {
    private String message;

    public RpcError() {
    }

    public RpcError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
