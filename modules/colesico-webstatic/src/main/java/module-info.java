module colesico.framework.webstatic {

    requires org.slf4j;

    requires transitive colesico.framework.http;
    requires transitive colesico.framework.resource;

    // classes

    exports colesico.framework.webstatic;
    exports colesico.framework.webstatic.internal to colesico.framework.ioc;

}