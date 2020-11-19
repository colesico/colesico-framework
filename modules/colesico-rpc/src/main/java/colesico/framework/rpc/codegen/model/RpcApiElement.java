package colesico.framework.rpc.codegen.model;

import colesico.framework.assist.codegen.model.ClassElement;

import java.util.ArrayList;
import java.util.List;

/**
 * RPC API  interface representation
 */
public class RpcApiElement {
    public static final String SCHEME_CLASS_SUFFIX = "RpcScheme";

    private final ClassElement originInterface;
    private final List<RpcApiMethodElement> rpcMethods = new ArrayList<>();

    public RpcApiElement(ClassElement originInterface) {
        this.originInterface = originInterface;
    }

    public void addMethod(RpcApiMethodElement method) {
        rpcMethods.add(method);
        method.setParentApi(this);
    }

    public String getSchemeClassName() {
        return originInterface.getSimpleName() + SCHEME_CLASS_SUFFIX;
    }

    public ClassElement getOriginInterface() {
        return originInterface;
    }

    public List<RpcApiMethodElement> getRpcMethods() {
        return rpcMethods;
    }
}
