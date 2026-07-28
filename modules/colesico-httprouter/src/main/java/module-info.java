
module colesico.framework.httprouter {

    requires static com.palantir.javapoet;
    requires static java.compiler;

    requires transitive colesico.framework.config;
    requires transitive colesico.framework.service;
    requires transitive colesico.framework.http;

    requires org.slf4j;

    exports colesico.framework.httprouter;
    exports colesico.framework.httprouter.assist;
    exports colesico.framework.httprouter.codegen;

    exports colesico.framework.httprouter.internal to colesico.framework.ioc;

}
