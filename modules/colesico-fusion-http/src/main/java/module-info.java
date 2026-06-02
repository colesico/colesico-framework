module colesico.framework.fusionhttp {

    requires org.slf4j;
    requires io.fusionauth.http;

    requires transitive colesico.framework.config;
    requires transitive colesico.framework.httpserver;

    exports colesico.framework.fusionhttp;
    exports colesico.framework.fusionhttp.internal to colesico.framework.ioc;

}