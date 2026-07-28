module colesico.framework.resource {

    requires static java.compiler;
    requires static com.palantir.javapoet;

    // Inherited in client projects
    requires transitive colesico.framework.profile;

    requires org.slf4j;

    // Exports
    exports colesico.framework.resource;
    exports colesico.framework.resource.assist;
    exports colesico.framework.resource.l10n;
    exports colesico.framework.resource.internal to colesico.framework.ioc;
    exports colesico.framework.resource.internal.l10n to colesico.framework.ioc;

}