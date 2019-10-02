package colesico.framework.example.routes;

import colesico.framework.http.HttpMethod;
import colesico.framework.router.RequestMethod;
import colesico.framework.router.Route;
import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.Weblet;

/**
 * POST request handling example.
 */
@Weblet
@Route("/my-form")
public class SubmitForm {

    /**
     * Customize HTTP request method form GET (by default) to POST
     * Corresponding URL POST http://localhost:8080/my-form/submit
     * @return
     */
    @RequestMethod(HttpMethod.POST)
    public HtmlResponse submit() {
        return new HtmlResponse("Submitted");
    }
}
