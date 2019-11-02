package colesico.framework.example.routes;

import colesico.framework.ioc.IocBuilder;
import colesico.framework.undertow.HttpServer;

public class Main {

    public static void main(String[] args) {
        IocBuilder.forProduction().instance(HttpServer.class).start();
    }
}
