package colesico.examples.web.routes;

import colesico.framework.router.Route;
import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.Weblet;

@Weblet
@Route("/my-non-relative_path")
public class NonRelative {

    // http://localhost:8080/my-non-relative_path/printParams.html

    @Route("/printParams.html")
    public HtmlResponse test(){
        return new HtmlResponse("Test!");
    }
}
