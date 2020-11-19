package colesico.framework.rpc.codegen.model;

import colesico.framework.assist.StrUtils;
import colesico.framework.assist.codegen.model.ParameterElement;
import colesico.framework.service.codegen.model.TeleVarElement;

import javax.lang.model.type.TypeMirror;

/**
 * RPC API method parameter element
 */
public class RpcApiParamElement {
    private final ParameterElement originParam;
    private RpcApiMethodElement parentMethod;

    /**
     * Parameter index 0..N
     */
    private final Integer paramIndex;

    /**
     * Bound tele param
     */
    private TeleVarElement teleParam;

    public RpcApiParamElement(ParameterElement originParam, Integer paramIndex) {
        this.originParam = originParam;
        this.paramIndex = paramIndex;
    }

    public String getterName() {
        return "getRpcParam" + paramIndex;
    }

    public String setterName() {
        return "setRpcParam" + paramIndex;
    }

    public String fieldName() {
        return "rpcParam" + paramIndex;
    }

    public TypeMirror getParamType() {
        return originParam.getOriginType();
    }

    public ParameterElement getOriginParam() {
        return originParam;
    }

    public RpcApiMethodElement getParentMethod() {
        return parentMethod;
    }

    public void setParentMethod(RpcApiMethodElement parentMethod) {
        this.parentMethod = parentMethod;
    }

    public TeleVarElement getTeleParam() {
        return teleParam;
    }

    public void setTeleParam(TeleVarElement teleParam) {
        this.teleParam = teleParam;
    }

    public Integer getParamIndex() {
        return paramIndex;
    }
}
