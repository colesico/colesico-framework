package colesico.framework.example.helloworld;

import colesico.framework.ioc.IocBuilder;
import colesico.framework.undertow.HttpServer;

/**
 * Main file for development
 */
public class MainDev {

    public static void main(String[] args) {
        IocBuilder
            .forDevelopment()
            .instance(HttpServer.class)
            .start();
    }
}
