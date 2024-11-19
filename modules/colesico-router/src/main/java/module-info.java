module colesico.framework.router {

    requires static java.compiler;
    requires static com.palantir.javapoet;

    requires transitive colesico.framework.service;
    requires transitive colesico.framework.http;
    requires transitive colesico.framework.config;

    requires org.slf4j;

    requires org.apache.commons.lang3;

    // classes

    exports colesico.framework.router;
    exports colesico.framework.router.assist;
    exports colesico.framework.router.internal to colesico.framework.ioc;
    exports colesico.framework.router.codegen;
}