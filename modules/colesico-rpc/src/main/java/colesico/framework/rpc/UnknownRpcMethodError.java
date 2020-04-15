package colesico.framework.rpc;

public class UnknownRpcMethodError extends Error {
    private String className;
    private String methodName;

    public UnknownRpcMethodError() {

    }

    public UnknownRpcMethodError(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    public UnknownRpcMethodError(String message, String className, String methodName) {
        super(message);
        this.className = className;
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
