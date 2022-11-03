package colesico.framework.rpc.codegen.model;

import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.rpc.RpcMethod;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * RPC API  interface representation
 */
public class RpcApiElement {
    public static final String ENVELOPE_PACK_CLASS_SUFFIX = "RpcEnvelopes";
    public static final String CLIENT_CLASS_SUFFIX = "RpcClient";

    private final ClassElement originInterface;
    private final List<RpcApiMethodElement> rpcMethods = new ArrayList<>();

    /**
     * RPC API Namespace
     */
    private final String namespace;

    /**
     * Custom RPC name
     *
     * @see RpcMethod
     */
    private final String rpcName;

    public RpcApiElement(ClassElement originInterface, String namespace, String rpcName) {
        this.originInterface = originInterface;
        this.namespace = namespace;
        this.rpcName = rpcName;
    }

    public void addMethod(RpcApiMethodElement method) {
        rpcMethods.add(method);
        method.setParentApi(this);
    }

    public String getNamespace() {
        return namespace;
    }

    public String rpcName() {
        if (StringUtils.isBlank(rpcName)) {
            return originInterface.getName();
        } else {
            return rpcName;
        }
    }

    public String getEnvelopePackClassSimpleName() {
        return originInterface.getSimpleName() + ENVELOPE_PACK_CLASS_SUFFIX;
    }

    public String getClientClassSimpleName() {
        return originInterface.getSimpleName() + CLIENT_CLASS_SUFFIX;
    }

    public String getClientClassName() {
        return originInterface.getPackageName() + '.' + originInterface.getSimpleName() + CLIENT_CLASS_SUFFIX;
    }

    public ClassElement getOriginInterface() {
        return originInterface;
    }

    public List<RpcApiMethodElement> getRpcMethods() {
        return rpcMethods;
    }
}
