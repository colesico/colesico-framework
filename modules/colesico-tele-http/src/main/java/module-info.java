
module colesico.framework.telehttp {

    requires static com.palantir.javapoet;
    requires static java.compiler;

    requires org.slf4j;

    requires transitive colesico.framework.config;
    requires transitive colesico.framework.service;
    requires transitive colesico.framework.http;
    requires transitive colesico.framework.router;
    requires transitive colesico.framework.security;
    requires transitive colesico.framework.translation;

    exports colesico.framework.telehttp;
    exports colesico.framework.telehttp.assist;
    exports colesico.framework.telehttp.origin;
    exports colesico.framework.telehttp.reader;
    exports colesico.framework.telehttp.writer;
    exports colesico.framework.telehttp.codegen;
    exports colesico.framework.telehttp.response;
    exports colesico.framework.telehttp.authentication;
    exports colesico.framework.telehttp.internal to colesico.framework.ioc;

    exports colesico.framework.telehttp.t9n;
    opens colesico.framework.telehttp.t9n;


}
