package colesico.framework.example.routes;

import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.Weblet;

/**
 * This weblet demonstrates implicit routes definitions.
 *
 * A service name begins with  "Index" prefix  is bound by default to the local (regarding package route) root route.
 * Route annotation equivalent is @Route("./")
 */
@Weblet
public class IndexOther {

    /**
     * The method name "index" is bound by default to the root to the route of the weblet.
     * Corresponding URL http://localhost:8080/api/v1.0    URL part  /api/v1.0 - derived  from the package route
     * If method name is not "index" it is possible to put  @Route("./") annotation to construct the same route as for
     * "index" method name
     */
    public HtmlResponse index(){
        return HtmlResponse.of("Index!");
    }

    /**
     * "other" method name  is bound by default to the local any route, i.e. corresponds to the @Route("*") annotation.
     * Corresponding URL http://localhost:8080/api/v1.0/[any path]
     */
    public HtmlResponse other(){
        return HtmlResponse.of("Other!");
    }
}
