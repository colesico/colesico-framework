package colesico.framework.restlet.teleapi;

import colesico.framework.telehttp.Origin;

public interface RestletOrigin<K, V> extends Origin<K, V> {

    /**
     * Auto origin
     */
    String AUTO = "RESTLET_AUTO";

}
