package colesico.framework.example.web.params;

import colesico.framework.router.Route;
import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.Weblet;

import java.text.MessageFormat;

@Weblet
public class GetParams {

    // http://localhost:8080/get-params/a-b?a=test&b=100

    @Route("a-b")
    public HtmlResponse printParams(String a, Integer b ){
        return new HtmlResponse(MessageFormat.format("a={0}, b={1}",a,b));
    }

    // http://localhost:8080/get-params/path/test/100
    @Route("path/:a/:b")
    public HtmlResponse printRouteParams(String a, Integer b ){
        return new HtmlResponse(MessageFormat.format("a={0}, b={1}",a,b));
    }

    // http://localhost:8080/get-params/path-s/foo/blabla
    @Route("path-s/*")
    public HtmlResponse printRouteSuffix(String routeSuffix ){
        return new HtmlResponse(MessageFormat.format("routeSuffix={0}",routeSuffix));
    }
}
