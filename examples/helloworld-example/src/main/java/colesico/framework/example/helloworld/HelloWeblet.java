package colesico.framework.example.helloworld;

import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.Weblet;

@Weblet
public class HelloWeblet {

    public static final String SAY_HELLO_TEXT = "Hello World!";
    public static final String SAY_HOLLA_TEXT = "Holla World!";

    // Browse the url: http://localhost:8080/hello-weblet/say-hello
    public HtmlResponse sayHello() {
        return new HtmlResponse(SAY_HELLO_TEXT);
    }

    // Browse the url: http://localhost:8080/hello-weblet/holla
    public HtmlResponse holla() {
        return new HtmlResponse(SAY_HOLLA_TEXT);
    }
}
