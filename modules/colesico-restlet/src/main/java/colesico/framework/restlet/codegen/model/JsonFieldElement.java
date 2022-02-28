package colesico.framework.restlet.codegen.model;

import colesico.framework.assist.StrUtils;
import colesico.framework.restlet.teleapi.jsonrequest.JsonField;
import colesico.framework.service.codegen.model.teleapi.TeleParameterElement;
import colesico.framework.telehttp.ParamName;
import org.apache.commons.lang3.StringUtils;

public class JsonFieldElement {

    private JsonRequestElement parentRequest;

    private final TeleParameterElement originParam;
    private final String name;

    public JsonFieldElement(TeleParameterElement originParam) {
        this.originParam = originParam;

        var jf = originParam.getOriginElement().getAnnotation(JsonField.class);
        if (jf != null && StringUtils.isNoneBlank(jf.unwrap().name())) {
            name = jf.unwrap().name();
        } else {
            var pn = originParam.getOriginElement().getAnnotation(ParamName.class);
            if (pn != null) {
                name = pn.unwrap().value();
            } else {
                name = originParam.getOriginElement().getName();
            }
        }

        checkFieldName(name);
    }

    private void checkFieldName(String name) {
        //TODO
    }

    public String getName() {
        return name;
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

    public TeleParameterElement getOriginParam() {
        return originParam;
    }

    public void setParentRequest(JsonRequestElement parentRequest) {
        this.parentRequest = parentRequest;
    }
}
