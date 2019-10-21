package colesico.framework.example.routes;

import colesico.framework.router.Route;
import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.Weblet;

/**
 * Weblet relative path example
 * see package-info.java  @Route annotation
 */
@Weblet
@Route("./relative")
public class RelativeComplex {

    // http://localhost:8080/api/v1.0/relative/say-hallo
    // /api/v1.0 + /relative + /say-hallo
    @Route("say-hallo")
    public HtmlResponse hallo() {
        return HtmlResponse.of("Hallo!");
    }

    // http://localhost:8080/api/v1.0/relative/say-hei
    @Route("./say-hei")
    // Equivalent @Route("say-hei")
    public HtmlResponse hei() {
        return HtmlResponse.of("Hei!");
    }

    // http://localhost:8080/api/v1.0/relative
    // The equivalent is the method name "index"
    @Route("./")
    public HtmlResponse welcome() {
        return HtmlResponse.of("Welcome!");
    }
}
