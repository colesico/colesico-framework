module colesico.framework.profile {

    // Compile time req.
    requires static java.compiler;
    requires static com.palantir.javapoet;

    // Runtime req.
    requires transitive colesico.framework.teleapi;
    requires transitive colesico.framework.config;

    requires org.slf4j;

    // Exports

    // API
    exports colesico.framework.profile;
    exports colesico.framework.profile.assist;
    exports colesico.framework.profile.internal;

    // Internals
}