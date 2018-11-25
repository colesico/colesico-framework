
module colesico.framework.http {

    //requires org.slf4j;
    requires slf4j.api;
    requires org.apache.commons.lang3;

    requires transitive  colesico.framework.ioc;

    exports colesico.framework.http;
    exports colesico.framework.http.internal to colesico.framework.ioc;

}