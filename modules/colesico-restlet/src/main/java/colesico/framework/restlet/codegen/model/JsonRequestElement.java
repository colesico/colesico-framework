package colesico.framework.restlet.codegen.model;

import colesico.framework.assist.StrUtils;
import colesico.framework.service.codegen.model.TeleMethodElement;

import java.util.ArrayList;
import java.util.List;

public class JsonRequestElement {
    public static final String JSON_REQUEST_CLASS_SUFFIX = "JsonRequest";

    private JsonPackElement parentPack;

    private final TeleMethodElement originMethod;

    private final List<JsonFieldElement> fields = new ArrayList<>();

    public JsonRequestElement(TeleMethodElement originMethod) {
        this.originMethod = originMethod;
    }

    public void addField(JsonFieldElement field) {
        fields.add(field);
        field.setParentRequest(this);
    }

    public List<JsonFieldElement> getFields() {
        return fields;
    }

    public JsonPackElement getParentPack() {
        return parentPack;
    }

    public void setParentPack(JsonPackElement parentPack) {
        this.parentPack = parentPack;
    }

    public TeleMethodElement getOriginMethod() {
        return originMethod;
    }

    public String getJsonRequestClassSimpleName() {
        return StrUtils.firstCharToUpperCase(originMethod.getName()) + JSON_REQUEST_CLASS_SUFFIX;
    }

    public String getJsonRequestClassName() {
        return parentPack.getOriginTeleFacade().getParentService().getOriginClass().getPackageName() + '.' +
                parentPack.getJsonPackClassSimpleName() + '.' +
                getJsonRequestClassSimpleName();
    }

}
