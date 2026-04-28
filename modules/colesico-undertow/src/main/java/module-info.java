module colesico.framework.undertow {

    requires org.slf4j;
    requires org.apache.commons.lang3;

    requires transitive undertow.core;
    requires xnio.api;

    requires transitive colesico.framework.config;
    requires transitive colesico.framework.httpservice;

    exports colesico.framework.undertow;
    exports colesico.framework.undertow.internal to colesico.framework.ioc;

}