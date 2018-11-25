package colesico.examples.web.helloworld;

import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.Weblet;

@Weblet
public class MyWeblet {

    //  http://localhost:8080/my-weblet/say-hello
    public HtmlResponse sayHello(){
        return new HtmlResponse("Hello from MyWeblet");
    }

}
