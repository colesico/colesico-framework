package colesico.framework.example.web.helloworld;

import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.Weblet;

@Weblet
public class MyWeblet {

    //  http://localhost:8080/my-weblet/say-hello
    public HtmlResponse sayHello(){
        return HtmlResponse.of("Hello from MyWeblet");
    }

}
