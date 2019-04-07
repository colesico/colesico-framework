package colesico.framework.example.helloworld;

import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.Weblet;

@Weblet
public class HelloWeblet {

    // Browse the url: http://localhost:8080/hello-weblet/say-hello
    public HtmlResponse sayHello(){
        return new HtmlResponse("Hello World!");
    }

    // Browse the url: http://localhost:8080/hello-weblet/holla
    public HtmlResponse holla(){
        return new HtmlResponse("Holla World!");
    }
}
