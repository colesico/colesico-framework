package colesico.framework.restlet.codegen.model;

import colesico.framework.assist.StrUtils;
import colesico.framework.service.codegen.model.TeleMethodElement;

import java.util.ArrayList;
import java.util.List;

public class JsonRequestElement {
    public static final String JSON_REQUEST_CLASS_SUFFIX = "JsonRequest";

    private JsonRequestPackElement parentPack;

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

    public JsonRequestPackElement getParentPack() {
        return parentPack;
    }

    public void setParentPack(JsonRequestPackElement parentPack) {
        this.parentPack = parentPack;
    }

    public TeleMethodElement getOriginMethod() {
        return originMethod;
    }

    public String getJsonRequestClassName() {
        return parentPack.getOriginTeleFacade().getParentService().getOriginClass().getPackageName() + '.' +
                parentPack.getJsonPackClassSimpleName() + '.' +
                StrUtils.firstCharToUpperCase(originMethod.getName()) + JSON_REQUEST_CLASS_SUFFIX;
    }

}
