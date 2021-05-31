package colesico.framework.weblet.teleapi;

import colesico.framework.telehttp.Origin;

public interface WebletOrigin<K,V> extends Origin<K,V> {

    /**
     * Auto origin
     */
    String AUTO = "WEBLET_AUTO";
}
