package colesico.framework.example.web;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;
import colesico.framework.undertow.HttpServer;

public class Main {

    public static void main(String[] args) {

        // Setup web builder
        // IocBuilder is not thread safe
        IocBuilder iocBuilder = IocBuilder.get();

        // Build web instance. Ioc instance is thread safe
        final Ioc ioc = iocBuilder.build();

        HttpServer httpServer = ioc.instance(HttpServer.class);
        httpServer.init();
        httpServer.start();
    }
}
