package colesico.framework.rpc.codegen.model;

import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.model.MethodElement;
import colesico.framework.rpc.RpcMethod;
import colesico.framework.service.codegen.model.teleapi.TeleMethodElement;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * RPC API interface method model
 */
public class RpcApiMethodElement {

    private RpcApiElement parentApi;

    public static final String RPC_REQUEST_CLASS_SUFFIX = "Request";
    public static final String RPC_RESPONSE_CLASS_SUFFIX = "Response";

    private final MethodElement originMethod;
    private final List<RpcApiParamElement> parameters = new ArrayList<>();

    /**
     * Bound tele method
     */
    private TeleMethodElement teleMethod;

    /**
     * Custom RPC name
     *
     * @see RpcMethod
     */
    private final String rpcName;

    /**
     * Method index within class or interface
     */
    protected int index = 0;

    public RpcApiMethodElement(MethodElement originMethod, String rpcName) {
        this.originMethod = originMethod;
        this.rpcName = rpcName;
        this.index = index;
    }

    public String getRequestClassSimpleName() {
        return StrUtils.firstCharToUpperCase(originMethod.getName()) + RPC_REQUEST_CLASS_SUFFIX+index;
    }

    public String getRequestClassName() {
        return parentApi.getOriginInterface().getPackageName() + '.' +
                parentApi.getEnvelopePackClassSimpleName() + '.' +
                getRequestClassSimpleName();
    }

    public String getResponseClassSimpleName() {
        return StrUtils.firstCharToUpperCase(originMethod.getName()) + RPC_RESPONSE_CLASS_SUFFIX+index;
    }

    public String getResponseClassName() {
        return parentApi.getOriginInterface().getPackageName() + '.' +
                parentApi.getEnvelopePackClassSimpleName() + '.' +
                getResponseClassSimpleName();
    }

    public void addParameter(RpcApiParamElement param) {
        parameters.add(param);
        param.setParentMethod(this);
    }

    public String rpcMethodName() {
        if (StringUtils.isBlank(rpcName)) {
            return originMethod.getName();
        } else {
            return rpcName;
        }
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
