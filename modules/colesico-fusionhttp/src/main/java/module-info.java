module colesico.framework.fusionhttp {

    requires org.slf4j;
    requires org.apache.commons.lang3;

    requires fusionauth-http;

    requires transitive colesico.framework.config;
    requires transitive colesico.framework.httpservice;
    requires io.fusionauth.http;

    exports colesico.framework.undertow;
    exports colesico.framework.undertow.internal to colesico.framework.ioc;

}