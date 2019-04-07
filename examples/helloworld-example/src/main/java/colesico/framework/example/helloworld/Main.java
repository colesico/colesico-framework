package colesico.framework.example.helloworld;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import colesico.framework.undertow.HttpServer;

public class Main {

    public static void main(String[] args) {

        // Build DI container instance.
        final Ioc ioc = IocBuilder.forProduction();

        // Obtain http server
        HttpServer httpServer = ioc.instance(HttpServer.class);

        // Init and start http server
        httpServer.init();
        httpServer.start();
    }
}
