module colesico.framework.fusionhttp {

    requires transitive colesico.framework.config;
    requires transitive colesico.framework.httpserver;
    requires transitive io.fusionauth.http;

    requires org.slf4j;

    exports colesico.framework.fusionhttp;
    exports colesico.framework.fusionhttp.internal to colesico.framework.ioc;

}