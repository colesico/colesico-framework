package colesico.framework.example.web.routes;

import colesico.framework.router.Route;
import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.Weblet;

/**
 * Weblet relative uri example
 * see package-info.java  @Route annotation
 */
@Weblet
public class Relative1 {

    // http://localhost:8080/my-service/v1.0/relative1/hi
    //
    public HtmlResponse hi() {
        return HtmlResponse.of("Hi!");
    }
}
