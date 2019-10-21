package colesico.framework.example.web.routes;

import colesico.framework.router.Route;
import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.Weblet;

/**
 * Root uri example
 */
@Weblet
public class Index {

    // Full URI: /
    public HtmlResponse index(){
        return HtmlResponse.of("Hello from Index");
    }

}
