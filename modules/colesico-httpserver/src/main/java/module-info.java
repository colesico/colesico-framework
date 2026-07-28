
module colesico.framework.httpserver {

    requires static com.palantir.javapoet;
    requires static java.compiler;

    requires transitive colesico.framework.http;
    requires transitive colesico.framework.httprouter;

    requires org.slf4j;

    exports colesico.framework.httpserver;
    exports colesico.framework.httpserver.internal to colesico.framework.ioc;

}
