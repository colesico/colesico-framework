
module colesico.framework.http {

    requires org.slf4j;
    requires transitive colesico.framework.ioc;

    exports colesico.framework.http;
    exports colesico.framework.http.assist;
    exports colesico.framework.http.internal to colesico.framework.ioc;

}