module colesico.framework.httpserver {

    requires transitive colesico.framework.router;

    requires org.slf4j;

    requires org.apache.commons.lang3;

    exports colesico.framework.httpserver;
    exports colesico.framework.httpserver.internal to colesico.framework.ioc;

}