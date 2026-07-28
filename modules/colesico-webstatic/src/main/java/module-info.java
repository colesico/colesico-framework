module colesico.framework.webstatic {

    requires transitive colesico.framework.http;
    requires transitive colesico.framework.resource;

    requires org.slf4j;

    exports colesico.framework.webstatic;
    exports colesico.framework.webstatic.internal to colesico.framework.ioc;

}