package colesico.framework.restlet.codegen.model;

import colesico.framework.service.codegen.model.TeleFacadeElement;

import java.util.ArrayList;
import java.util.List;

public class JsonRequestPackElement {
    public static final String JSON_PACK_CLASS_SUFFIX = "JsonPack";

    private final TeleFacadeElement originTeleFacade;

    private List<JsonRequestElement> requests = new ArrayList<>();

    public JsonRequestPackElement(TeleFacadeElement originTeleFacade) {
        this.originTeleFacade = originTeleFacade;
    }

    public void addRequest(JsonRequestElement object) {
        requests.add(object);
        object.setParentPack(this);
    }

    public TeleFacadeElement getOriginTeleFacade() {
        return originTeleFacade;
    }

    public List<JsonRequestElement> getRequests() {
        return requests;
    }

    public String getJsonPackClassSimpleName() {
        return originTeleFacade.getParentService().getOriginClass().getSimpleName() + JSON_PACK_CLASS_SUFFIX;
    }
}