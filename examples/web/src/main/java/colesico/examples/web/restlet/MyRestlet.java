package colesico.examples.web.restlet;

import colesico.framework.restlet.Restlet;

@Restlet
public class MyRestlet {

    public MyDataBean get(String message, Long code){
        return new MyDataBean(message,code);
    }
}
