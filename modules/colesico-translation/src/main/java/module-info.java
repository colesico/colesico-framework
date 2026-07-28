module colesico.framework.translation {

    // Compile time
    requires static java.compiler;
    requires static com.palantir.javapoet;

    // Inherited in client projects
    requires transitive colesico.framework.resource;

    requires org.slf4j;

    // Exports

    // API
    exports colesico.framework.translation;
    exports colesico.framework.translation.assist.lang;
    exports colesico.framework.translation.assist.propbundle;

    // Internals
    exports colesico.framework.translation.internal to colesico.framework.ioc;


}