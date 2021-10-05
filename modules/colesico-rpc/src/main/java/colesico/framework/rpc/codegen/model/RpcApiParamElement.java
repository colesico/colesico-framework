package colesico.framework.rpc.codegen.model;

import colesico.framework.assist.codegen.model.ParameterElement;
import colesico.framework.service.codegen.model.TeleParameterElement;

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
    private TeleParameterElement teleParam;

    public RpcApiParamElement(ParameterElement originParam, Integer paramIndex) {
        this.originParam = originParam;
        this.paramIndex = paramIndex;
    }

    public String getterName() {
        return "getParam" + paramIndex;
    }

    public String setterName() {
        return "setParam" + paramIndex;
    }

    public String fieldName() {
        return "param" + paramIndex;
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

    public TeleParameterElement getTeleParam() {
        return teleParam;
    }

    public void setTeleParam(TeleParameterElement teleParam) {
        this.teleParam = teleParam;
    }

    public Integer getParamIndex() {
        return paramIndex;
    }
}
