package colesico.framework.example.routes;

import colesico.framework.router.Route;
import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.Weblet;

/**
 * Weblet relative uri example
 * see package-info.java  @Route annotation
 */
@Weblet
@Route("relative-route")
public class RelativeSimple {

    // http://localhost:8080/api/v1.0/relative-route/say-hola
    @Route("say-hola")
    public HtmlResponse hola() {
        return HtmlResponse.of("Hola!");
    }
}
