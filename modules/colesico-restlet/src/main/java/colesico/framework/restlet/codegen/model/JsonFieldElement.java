package colesico.framework.restlet.codegen.model;

import colesico.framework.assist.StrUtils;
import colesico.framework.service.codegen.model.TeleParamElement;

public class JsonFieldElement {

    private JsonRequestElement parentRequest;

    private final TeleParamElement originParam;

    public JsonFieldElement(TeleParamElement originParam) {
        this.originParam = originParam;
    }

    public String getName() {
        return originParam.getOriginParam().getName();
    }

    public String getterName() {
        return "get" + StrUtils.firstCharToUpperCase(getName());
    }

    public String setterName() {
        return "set" + StrUtils.firstCharToUpperCase(getName());
    }

    public JsonRequestElement getParentRequest() {
        return parentRequest;
    }

    public TeleParamElement getOriginParam() {
        return originParam;
    }

    public void setParentRequest(JsonRequestElement parentRequest) {
        this.parentRequest = parentRequest;
    }
}
