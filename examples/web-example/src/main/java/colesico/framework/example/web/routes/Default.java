package colesico.framework.example.web.routes;

import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.Weblet;

/**
 * Weblet default uri example
 */
@Weblet
public class Default {

    // http://localhost:8080/default/hello
    public HtmlResponse hello(){
        return new HtmlResponse("Hello");
    }
}
