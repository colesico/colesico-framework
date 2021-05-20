package colesico.framework.restlet.codegen.model;

import colesico.framework.assist.StrUtils;
import colesico.framework.service.codegen.model.TeleParamElement;

public class JsonFieldElement {

    private JsonRequestElement parentRequest;

    private final TeleParamElement originParam;

    public JsonFieldElement(TeleParamElement originParam) {
        this.originParam = originParam;
    }

    public String getterName() {
        return "get" + StrUtils.firstCharToUpperCase(originParam.getOriginParam().getName());
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
