
module colesico.framework.http {

    requires transitive colesico.framework.ioc;

    requires org.slf4j;

    exports colesico.framework.http;
    exports colesico.framework.http.assist;
    exports colesico.framework.http.internal to colesico.framework.ioc;

}