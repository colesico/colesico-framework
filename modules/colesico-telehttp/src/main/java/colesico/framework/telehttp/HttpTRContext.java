package colesico.framework.telehttp;

import colesico.framework.http.HttpRequest;
import colesico.framework.router.RouterContext;

/**
 * Basic tele-reading context for interaction via http
 */
abstract public class HttpTRContext {

    /**
     * Parameter name
     */
    private final String name;

    /**
     * Origin facade to read parameter from it
     */
    private final OriginFacade originFacade;

    public HttpTRContext(String name, OriginFacade originFacade) {
        this.name = name;
        this.originFacade = originFacade;
    }

    /**
     * Parameter name
     */
    public final String getName() {
        return name;
    }

    /**
     * Origin facade
     */
    public OriginFacade getOriginFacade() {
        return originFacade;
    }

    /**
     * Parameter value
     */
    public final String getString(RouterContext routerContext, HttpRequest httpRequest) {
        return originFacade.getString(name, routerContext, httpRequest);
    }
}
