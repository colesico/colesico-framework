module colesico.framework.profile {

    // Compile time req.
    requires static java.compiler;
    requires static com.palantir.javapoet;

    // Runtime req.
    requires transitive colesico.framework.teleapi;
    requires transitive colesico.framework.config;

    requires org.slf4j;

    requires org.apache.commons.lang3;
    requires java.desktop;

    // Exports

    // API
    exports colesico.framework.profile;

    // Internals
    exports colesico.framework.profile.internal to colesico.framework.ioc;
}