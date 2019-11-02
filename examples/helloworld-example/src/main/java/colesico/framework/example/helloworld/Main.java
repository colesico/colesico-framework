package colesico.framework.example.helloworld;

import colesico.framework.ioc.IocBuilder;
import colesico.framework.undertow.HttpServer;

/**
 * Main file for production
 */
public class Main {

    public static void main(String[] args) {
        IocBuilder
            .forProduction()
            .instance(HttpServer.class)
            .start();
    }
}
