module colesico.framework.undertow {

    requires transitive colesico.framework.config;
    requires transitive colesico.framework.router;

    //requires org.slf4j;
    requires slf4j.api;
    requires org.apache.commons.lang3;

    requires undertow.core;
    requires xnio.api;

    exports colesico.framework.undertow;
    exports colesico.framework.undertow.internal to colesico.framework.ioc;

}