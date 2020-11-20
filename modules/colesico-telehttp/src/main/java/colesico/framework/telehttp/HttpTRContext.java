package colesico.framework.telehttp;

import colesico.framework.http.HttpContext;

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

    public static HttpTRContext of(String name, OriginFacade originFacade) {
        return new HttpTRContext(name, originFacade) {
        };
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

}
