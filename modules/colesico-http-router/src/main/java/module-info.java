
module colesico.framework.router {

    requires static com.palantir.javapoet;
    requires static java.compiler;

    requires org.slf4j;

    requires transitive colesico.framework.config;
    requires transitive colesico.framework.service;
    requires transitive colesico.framework.http;

    exports colesico.framework.router;
    exports colesico.framework.router.assist;
    exports colesico.framework.router.codegen;

    exports colesico.framework.router.internal to colesico.framework.ioc;

}
