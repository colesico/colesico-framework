package colesico.framework.restlet.teleapi;

import colesico.framework.telehttp.Origin;

public interface RestletOrigin<K, V> extends Origin<K, V> {

    /**
     * Auto origin
     */
    String AUTO = "RESTLET_AUTO";

    /**
     * Json field Origin.
     *
     * @see colesico.framework.restlet.teleapi.jsonrequest.JsonField
     * @see colesico.framework.restlet.teleapi.origin.JsonFieldOrigin
     */
    String JSON_FIELD = "JSON_FIELD";
}
