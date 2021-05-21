package colesico.framework.restlet.codegen.model;

import colesico.framework.assist.StrUtils;
import colesico.framework.restlet.teleapi.jsonrequest.JsonField;
import colesico.framework.service.codegen.model.TeleParamElement;
import colesico.framework.telehttp.ParamName;
import org.apache.commons.lang3.StringUtils;

public class JsonFieldElement {

    private JsonRequestElement parentRequest;

    private final TeleParamElement originParam;
    private final String name;

    public JsonFieldElement(TeleParamElement originParam) {
        this.originParam = originParam;

        var jf = originParam.getOriginParam().getAnnotation(JsonField.class);
        if (jf != null && StringUtils.isNoneBlank(jf.unwrap().name())) {
            name = jf.unwrap().name();
        } else {
            var pn = originParam.getOriginParam().getAnnotation(ParamName.class);
            if (pn != null) {
                name = pn.unwrap().value();
            } else {
                name = originParam.getOriginParam().getName();
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

    public TeleParamElement getOriginParam() {
        return originParam;
    }

    public void setParentRequest(JsonRequestElement parentRequest) {
        this.parentRequest = parentRequest;
    }
}
