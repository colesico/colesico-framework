package colesico.framework.rpc.teleapi;

public interface RpcDispatcher {
    void dispatch(String className, String methodName);
}
