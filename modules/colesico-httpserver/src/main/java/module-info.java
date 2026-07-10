
module colesico.framework.httpserver {

    requires static com.palantir.javapoet;
    requires static java.compiler;

    requires org.slf4j;

    requires transitive colesico.framework.http;
    requires transitive colesico.framework.httprouter;

    exports colesico.framework.httpserver;
    exports colesico.framework.httpserver.internal to colesico.framework.ioc;

}
