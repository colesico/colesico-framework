package colesico.framework.restlet.teleapi;

import colesico.framework.http.HttpRequest;
import colesico.framework.router.RouterContext;
import colesico.framework.weblet.teleapi.OriginFacade;

public final class RestletTRContext {

    /**
     * Parameter name
     */
    private final String name;

    /**
     * Origin facade to read parameter from it
     */
    private final OriginFacade originFacade;

    /**
     * Custom reader class or null.
     * If null - default reader will be used to read the parameter
     */
    private final Class<? extends RestletTeleReader> readerClass;

    public RestletTRContext(String name, OriginFacade originFacade, Class<? extends RestletTeleReader> readerClass) {
        this.name = name;
        this.originFacade = originFacade;
        this.readerClass = readerClass;
    }

    /**
     * Parameter name
     */
    public final String getName() {
        return name;
    }

    public Class<? extends RestletTeleReader> getReaderClass() {
        return readerClass;
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
