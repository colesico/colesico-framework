package colesico.framework.example.helloworld;

import colesico.framework.ioc.IocBuilder;
import colesico.framework.undertow.HttpServer;

/**
 * Main file for production lunches
 */
public class Main {

    public static void main(String[] args) {

        // Obtain http server from DI container
        HttpServer httpServer = IocBuilder.forProduction().instance(HttpServer.class);

        // Init and start http server
        httpServer.init();
        httpServer.start();
    }
}
