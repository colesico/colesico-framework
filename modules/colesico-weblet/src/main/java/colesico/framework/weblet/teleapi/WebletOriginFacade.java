package colesico.framework.weblet.teleapi;


import colesico.framework.telehttp.Origin;
import colesico.framework.telehttp.OriginFacade;


/**
 * Weblet specific origin facade
 */
abstract public class WebletOriginFacade extends OriginFacade {

    public WebletOriginFacade(Origin origin) {
        super(origin);
    }
}
