module colesico.framework.undertow {

    requires transitive colesico.framework.config;
    requires transitive colesico.framework.httpserver;

    requires org.slf4j;

    requires org.apache.commons.lang3;

    requires undertow.core;
    requires xnio.api;


    exports colesico.framework.undertow;
    exports colesico.framework.undertow.internal;

}