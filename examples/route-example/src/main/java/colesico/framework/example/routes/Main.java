package colesico.framework.example.routes;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import colesico.framework.undertow.HttpServer;

public class Main {

    public static void main(String[] args) {
        final Ioc ioc = IocBuilder.forProduction();
        HttpServer httpServer = ioc.instance(HttpServer.class);
        httpServer.init().start();
    }
}
