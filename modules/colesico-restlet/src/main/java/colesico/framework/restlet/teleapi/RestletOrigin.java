package colesico.framework.restlet.teleapi;

import colesico.framework.telehttp.Origin;

public interface RestletOrigin extends Origin {

    /**
     * Auto origin
     */
    String AUTO = "RESTLET_AUTO";

    /**
     * From body json field
     */
    String JSON_FIELD = "JSON_FIELD";
}
