module colesico.framework.security {

    // Compile time req.
    requires static java.compiler;
    requires static com.squareup.javapoet;

    // Runtime req.
    requires transitive colesico.framework.service;
    requires transitive colesico.framework.config;

    requires org.slf4j;

    requires org.apache.commons.lang3;

    // Exports
    exports colesico.framework.security;
    exports colesico.framework.security.assist;
    exports colesico.framework.security.teleapi;

    exports colesico.framework.security.internal to colesico.framework.ioc;

}