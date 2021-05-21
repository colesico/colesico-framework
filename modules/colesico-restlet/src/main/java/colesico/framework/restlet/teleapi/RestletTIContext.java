package colesico.framework.restlet.teleapi;

import colesico.framework.restlet.teleapi.jsonrequest.JsonRequest;
import colesico.framework.telehttp.HttpTIContext;

/**
 * Restlet tele-invocation context
 */
public final class RestletTIContext extends HttpTIContext {

    public static final String OF_METHOD = "of";

    private final Class<? extends JsonRequest> jsonRequestClass;

    private RestletTIContext(Class<? extends JsonRequest> jsonRequestClass) {
        this.jsonRequestClass = jsonRequestClass;
    }

    public Class<? extends JsonRequest> getJsonRequestClass() {
        return jsonRequestClass;
    }

    public static RestletTIContext of(Class<? extends JsonRequest> jsonRequestClass) {
        return new RestletTIContext(jsonRequestClass);
    }

    public static RestletTIContext of() {
        return new RestletTIContext(null);
    }

}
