package colesico.framework.example.web.routes;

import colesico.framework.router.Route;
import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.Weblet;

/**
 * Weblet relative path example
 * see package-info.java  @Route annotation
 */
@Weblet
@Route("./relative")
public class Relative2 {

    // http://localhost:8080/my-service/v1.0/relative/say-hi
    // /my-service/v1.0 + /relative + /say-hi
    @Route("say-hi")
    public HtmlResponse hi() {
        return HtmlResponse.of("Hi from 1");
    }

    // http://localhost:8080/my-service/v1.0/relative/say-hi-2
    @Route("./say-hi-2")
    public HtmlResponse hi2() {
        return HtmlResponse.of("Hi from 2");
    }

    // http://localhost:8080/my-service/v1.0/relative
    @Route("./")
    public HtmlResponse hi3() {
        return HtmlResponse.of("Hi from weblet index!");
    }
}
