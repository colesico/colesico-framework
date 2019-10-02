package colesico.framework.example.routes;

import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.Weblet;

/**
 * Weblet default route example.
 * <p>
 * Here is no @Route annotation on weblet. It is assumed that the route to be the weblet name in camel case transformed to snake case:
 * Default -> default
 */
@Weblet
public class Default {
    /**
     * The route for the method without @Route annotation is derived from method name transformed to snake case notation.
     * Corresponding URL GET http://localhost:8080/api/v1.0/default/hello    /api/v1.0 - part from package route
     */
    public HtmlResponse hello() {
        return new HtmlResponse("Hello world");
    }

}
