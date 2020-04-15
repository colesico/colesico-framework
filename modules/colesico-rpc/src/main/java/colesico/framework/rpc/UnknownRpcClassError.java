package colesico.framework.rpc;

public class UnknownRpcClassError extends Error {
    private String className;

    public UnknownRpcClassError() {
    }

    public UnknownRpcClassError(String className) {
        this.className = className;
    }

    public UnknownRpcClassError(String message, String className) {
        super(message);
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
