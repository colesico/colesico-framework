package colesico.framework.example.routes;

import colesico.framework.router.Route;
import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.Weblet;

/**
 * Absolute route starts with '/'
 */
@Weblet
@Route("/absolute-route")
public class AbsoluteRoute {

    // This is absolute route, starts with '/'
    // http://localhost:8080/say-hi.html
    @Route("/say-hi.html")
    public HtmlResponse hi() {
        return HtmlResponse.of("Hi!");
    }

    // This is relative route regarding weblet route.
    // Relative route is not starts with '/', or may starts from './'
    // http://localhost:8080/absolute-route/say-hello.html
    @Route("say-hello.html")
    public HtmlResponse hello() {
        return HtmlResponse.of("Hello!");
    }

}
