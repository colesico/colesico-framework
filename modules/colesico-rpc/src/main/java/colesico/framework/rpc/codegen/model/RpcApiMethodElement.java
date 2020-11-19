package colesico.framework.rpc.codegen.model;

import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.model.MethodElement;
import colesico.framework.service.codegen.model.TeleMethodElement;

import java.util.ArrayList;
import java.util.List;

/**
 * RPC API interface method model
 */
public class RpcApiMethodElement {

    private RpcApiElement parentApi;

    public static final String RPC_REQUEST_CLASS_SUFFIX = "RpcRequest";
    public static final String RPC_RESPONSE_CLASS_SUFFIX = "RpcResponse";

    private final MethodElement originMethod;
    private final List<RpcApiParamElement> parameters = new ArrayList<>();

    /**
     * Bound tele method
     */
    private TeleMethodElement teleMethod;

    public RpcApiMethodElement(MethodElement originMethod) {
        this.originMethod = originMethod;
    }

    public String getRequestClassSimpleName() {
        return StrUtils.firstCharToUpperCase(originMethod.getName()) + RPC_REQUEST_CLASS_SUFFIX;
    }

    public String getRequestClassName() {
        return parentApi.getOriginInterface().getPackageName() + '.' +
                parentApi.getSchemeClassName() + '.' +
                StrUtils.firstCharToUpperCase(originMethod.getName()) + RPC_REQUEST_CLASS_SUFFIX;
    }

    public String getResponseClassSimpleName() {
        return StrUtils.firstCharToUpperCase(originMethod.getName()) + RPC_RESPONSE_CLASS_SUFFIX;
    }

    public String getResponseClassName() {
        return parentApi.getOriginInterface().getPackageName() + '.' +
                parentApi.getSchemeClassName() + '.' +
                StrUtils.firstCharToUpperCase(originMethod.getName()) + RPC_RESPONSE_CLASS_SUFFIX;
    }

    public void addParameter(RpcApiParamElement param) {
        parameters.add(param);
        param.setParentMethod(this);
    }

    public String rpcMethodName() {
        return originMethod.getName();
    }

    public RpcApiElement getParentApi() {
        return parentApi;
    }

    public void setParentApi(RpcApiElement parentApi) {
        this.parentApi = parentApi;
    }

    public MethodElement getOriginMethod() {
        return originMethod;
    }

    public TeleMethodElement getTeleMethod() {
        return teleMethod;
    }

    public void setTeleMethod(TeleMethodElement teleMethod) {
        this.teleMethod = teleMethod;
    }

    public List<RpcApiParamElement> getParameters() {
        return parameters;
    }
}
