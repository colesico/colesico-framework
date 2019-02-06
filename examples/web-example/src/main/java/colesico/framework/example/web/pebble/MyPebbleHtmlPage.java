package colesico.framework.example.web.pebble;

import colesico.framework.htmlrenderer.HtmlRenderer;
import colesico.framework.ioc.Classed;
import colesico.framework.pebble.PebbleRenderer;
import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.Weblet;

@Weblet
public class MyPebbleHtmlPage {

    private final HtmlRenderer<String> htmlRender;

    public MyPebbleHtmlPage(@Classed(PebbleRenderer.class) HtmlRenderer<String> htmlRenderer) {
        this.htmlRender = htmlRenderer;
    }

    //  http://localhost:8080/my-pebble-html-page/print?val=10
    public HtmlResponse print(Integer val){
        return htmlRender.render("$tmplRoot/MyPebbleTemplate",val);
    }
}
