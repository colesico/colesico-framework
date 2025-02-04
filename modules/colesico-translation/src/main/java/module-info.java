module colesico.framework.translation {

    // Compile time req.
    requires static java.compiler;
    requires static com.palantir.javapoet;

    requires org.slf4j;

    requires org.apache.commons.lang3;

    requires transitive colesico.framework.resource;
    //  requires transitive cache2k.api;

    // Exports

    // API
    exports colesico.framework.translation;
    exports colesico.framework.translation.assist.lang;
    exports colesico.framework.translation.assist.propbundle;

    // Internals
    exports colesico.framework.translation.internal to colesico.framework.ioc;


}