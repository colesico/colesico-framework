package colesico.examples.web.routes;

import colesico.framework.router.Route;
import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.Weblet;

/**
 * Root uri example
 */
@Weblet
public class Index {

    // Full URI: /
    @Route("/")
    public HtmlResponse hello(){
        return new HtmlResponse("Hello from Index");
    }

}
