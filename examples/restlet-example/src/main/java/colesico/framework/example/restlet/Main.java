package colesico.framework.example.restlet;

import colesico.framework.ioc.IocBuilder;
import colesico.framework.undertow.HttpServer;

public class Main {

    public static void main(String[] args) {
        // Obtain http server
        HttpServer httpServer = IocBuilder.forProduction().instance(HttpServer.class);

        // Init and start
        httpServer.init();
        httpServer.start();
    }
}
